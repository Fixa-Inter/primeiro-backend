package com.servlet;

import com.DAO.InstituicaoDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Filtro;
import com.model.Instituicao;
import com.model.enums.OperacaoFiltro;
import com.model.enums.TipoInstituicao;
import com.service.HashSenha;
import com.utils.Parametros;
import com.utils.RegexUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

// Controla o CRUD de instituições na área restrita
@WebServlet(name = "InstituicaoServlet", value = "/instituicoes")
public class InstituicaoServlet extends HttpServlet{

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/restrita/instituicoes.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/restrita/cadastro-instituicao.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/restrita/editar-instituicao.jsp";

    // Abre a lista, o formulário de cadastro ou o de edição, conforme a action
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        boolean erro = true;
        String destino = null;

        if (action == null) action = "read";
        try {
            if (action.equals("read")) {
                listarInstituicoes(request, response);
                destino = PAGINA_PRINCIPAL;
            } else if (action.equals("create")) {
                destino = PAGINA_CADASTRO;
            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (InstituicaoDAO dao = new InstituicaoDAO()) {
                    Instituicao instituicao = dao.pesquisarPorId(id);
                    if (instituicao == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    request.setAttribute("instituicao", instituicao);
                }
                destino = PAGINA_EDICAO;
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            erro = false;
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);
        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        if (erro) response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        else request.getRequestDispatcher(destino).forward(request, response);
    }

    // Executa o cadastro, a edição ou a exclusão, conforme a action
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException, ExcecaoDeJSP{

        String action = request.getParameter("action");
        try {

            if ("create".equals(action)) {
                cadastrarInstituicao(request, response);
            } else if ("update".equals(action)) {
                atualizarInstituicao(request, response);
            } else if ("delete".equals(action)){
                deletarInstituicao(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
        }
    }

    // Lista instituições com filtros, ordenação e pesquisa da requisição
    private void listarInstituicoes(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (InstituicaoDAO dao = new InstituicaoDAO()) {

            List<Filtro> filtros = new ArrayList<>();

            String[] campos = request.getParameterValues("campoFiltro");
            String[] valores = request.getParameterValues("valorFiltro");
            String[] operacoes = request.getParameterValues("operacaoFiltro");

            String campoSequencia;
            String direcaoSequencia;

            String ordenacao = request.getParameter("ordenacao");

            String pesquisa = request.getParameter("pesquisa");

            String removerFiltroParam = request.getParameter("removerFiltro");
            Integer indiceRemover = null;
            if (removerFiltroParam != null
                    && removerFiltroParam.matches("\\d+")) {

                indiceRemover =
                        Integer.parseInt(removerFiltroParam);
            }

            if (campos != null
                    && valores != null
                    && operacoes != null
                    && campos.length == valores.length
                    && campos.length == operacoes.length) {

                for (int i = 0; i < campos.length; i++) {

                    if (indiceRemover != null
                            && i == indiceRemover) {

                        continue;
                    }

                    String campo = campos[i].trim().toUpperCase();
                    String valor = valores[i].trim();

                    // Ignora filtro sem valor ou com campo inexistente
                    if (valor.isEmpty()
                            || !InstituicaoDAO.camposFiltraveis.containsKey(campo)) {

                        continue;
                    }

                    OperacaoFiltro operacao;

                    try {
                        operacao = OperacaoFiltro.valueOf(operacoes[i].trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        continue;
                    }

                    // Ignora operação que o campo não aceita
                    List<OperacaoFiltro> operacoesPermitidas =
                            InstituicaoDAO.operacoesPorCampo.get(campo);

                    if (operacoesPermitidas == null
                            || !operacoesPermitidas.contains(operacao)) {

                        continue;
                    }

                    Object valorConvertido =
                            dao.converterValor(campo, valor);

                    // Ignora valor que não converte para o tipo do campo
                    if (valorConvertido == null) {
                        continue;
                    }

                    filtros.add(new Filtro(
                            campo,
                            valorConvertido,
                            operacao
                    ));
                }
            }

            if (ordenacao != null
                    && ordenacao.toUpperCase().matches("[A-Z_]+-(ASC|DESC)")) {

                String[] partesOrdenacao = ordenacao.split("-");

                campoSequencia = partesOrdenacao[0].toUpperCase();
                direcaoSequencia = partesOrdenacao[1].toUpperCase();

            } else {

                campoSequencia = null;
                direcaoSequencia = null;
            }

            List<Instituicao> instituicoes = dao.listar(
                    filtros,
                    campoSequencia,
                    direcaoSequencia,
                    pesquisa
            );

            request.setAttribute("instituicoes", instituicoes);
            request.setAttribute("filtros", filtros);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Cadastra uma instituição com os dados do formulário
    private void cadastrarInstituicao(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException, ExcecaoDeJSP{

        String nome = Parametros.obrigatorio(request, "nome");
        String emailCorporativo = Parametros.obrigatorio(request, "emailCorporativo");
        if (!RegexUtils.validarEmail(emailCorporativo)) {
            throw ExcecaoDeJSP.valorInvalido("emailCorporativo");
        }

        // O domínio não é digitado, sai do e-mail
        String dominioEmail = emailCorporativo.substring(emailCorporativo.indexOf("@") + 1);

        Integer tipoInstituicao = Parametros.inteiro(request, "tipoInstituicao");

        String senha = Parametros.senha(request, "senha");

        Instituicao instituicao = new Instituicao(nome, emailCorporativo, TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail, HashSenha.gerarHash(senha));

        try (InstituicaoDAO dao = new InstituicaoDAO()) {

            if (dao.pesquisarPorDominioEmail(dominioEmail) != null){
                throw ExcecaoDeJSP.dominioDuplicado();
            }

            if (dao.pesquisarPorEmailCorporativo(emailCorporativo) != null){
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.cadastrar(instituicao, request);

            response.sendRedirect(
                    request.getContextPath() + "/instituicoes"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Exclui uma instituição pelo id da requisição
    private void deletarInstituicao(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Parametros.inteiro(request, "id");

        try (InstituicaoDAO dao = new InstituicaoDAO()){
            dao.remover(id, request);

            response.sendRedirect(
                    request.getContextPath() + "/instituicoes"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Atualiza uma instituição com os dados do formulário
    private void atualizarInstituicao(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException, ExcecaoDeJSP{

        int id = Parametros.inteiro(request, "id");

        String nome = Parametros.obrigatorio(request, "nome");
        String emailCorporativo = Parametros.obrigatorio(request, "emailCorporativo");
        if (!RegexUtils.validarEmail(emailCorporativo)) {
            throw ExcecaoDeJSP.valorInvalido("emailCorporativo");
        }

        // O domínio não é digitado, sai do e-mail
        String dominioEmail = emailCorporativo.substring(emailCorporativo.indexOf("@") + 1);

        Integer tipoInstituicao = Parametros.inteiro(request, "tipoInstituicao");

        try (InstituicaoDAO dao = new InstituicaoDAO()) {

            // Busca o registro atual no banco
            Instituicao original = dao.pesquisarPorId(id);

            // Senha em branco mantém a atual, preenchida troca
            String senha = Parametros.senhaOpcional(request, "senha");
            String senhaHash = (senha == null)
                    ? original.getSenhaHash()
                    : HashSenha.gerarHash(senha);

            // Monta o objeto com os dados novos
            Instituicao alterado = new Instituicao(id, nome, emailCorporativo, original.getDataCadastro(), TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail, senhaHash);

            Instituicao instituicaoApoioDominio = dao.pesquisarPorDominioEmail(dominioEmail);
            Instituicao instituicaoApoioEmail = dao.pesquisarPorEmailCorporativo(emailCorporativo);

            if (instituicaoApoioDominio != null && instituicaoApoioDominio.getId() != id){
                throw ExcecaoDeJSP.dominioDuplicado();
            }

            if (instituicaoApoioEmail != null && instituicaoApoioEmail.getId() != id){
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.atualizar(original, alterado, request);

            response.sendRedirect(
                    request.getContextPath() + "/instituicoes"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
