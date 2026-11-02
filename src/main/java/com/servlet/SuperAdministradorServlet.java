package com.servlet;

import com.DAO.SuperAdministradorDAO;
import com.DTO.SuperAdminDTO;
import com.exception.ExcecaoDeJSP;
import com.model.Filtro;
import com.model.SuperAdministrador;
import com.model.enums.OperacaoFiltro;
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

// Controla o CRUD de super administradores na área restrita
@WebServlet(name = "SuperAdministradorServlet", value = "/superAdmin")
public class SuperAdministradorServlet extends HttpServlet{

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/restrita/super-admin.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/restrita/cadastro-super-admin.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/restrita/editar-super-admin.jsp";

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
                listarAdmins(request, response);
                destino = PAGINA_PRINCIPAL;
            } else if (action.equals("create")) {
                destino = PAGINA_CADASTRO;
            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (SuperAdministradorDAO dao = new SuperAdministradorDAO()) {
                    SuperAdministrador superAdministrador = dao.pesquisarPorId(id);
                    if (superAdministrador == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    request.setAttribute("superAdmin", superAdministrador);
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
    ) throws ServletException, IOException {

        String action = request.getParameter("action");
        try{
            if ("create".equals(action)) {
                cadastrarAdmin(request, response);
            } else if ("update".equals(action)) {
                atualizarAdmin(request, response);
            } else if ("delete".equals(action)){
                deletarAdmin(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
        }
    }

    // Lista os super administradores com filtros, ordenação e pesquisa da requisição
    private void listarAdmins(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (SuperAdministradorDAO dao = new SuperAdministradorDAO()) {

            List<Filtro> filtros = new ArrayList<>();

            String[] campos = request.getParameterValues("campoFiltro");
            String[] valores = request.getParameterValues("valorFiltro");
            String[] operacoes = request.getParameterValues("operacaoFiltro");

            String campoSequencia;
            String direcaoSequencia;

            String ordenacao = request.getParameter("ordenacao");

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
                            || !SuperAdministradorDAO.camposFiltraveis.containsKey(campo)) {

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
                            SuperAdministradorDAO.operacoesPorCampo.get(campo);

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

            List<SuperAdministrador> superAdmins = dao.buscar(
                    filtros,
                    campoSequencia,
                    direcaoSequencia,
                    request.getParameter("pesquisa")
            );

            request.setAttribute("superAdmins", superAdmins);
            request.setAttribute("filtros", filtros);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Cadastra um super administrador com os dados do formulário
    private void cadastrarAdmin(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String nome = Parametros.obrigatorio(request, "nome");

        String email = Parametros.obrigatorio(request, "email");
        if (!RegexUtils.validarEmail(email)) {
            throw ExcecaoDeJSP.valorInvalido("email");
        }

        String senha = Parametros.senha(request, "senha");

        SuperAdministrador superAdministrador = new SuperAdministrador(
                nome,
                HashSenha.gerarHash(senha),
                email
        );

        try (SuperAdministradorDAO dao = new SuperAdministradorDAO()) {

            if (dao.pesquisarPorEmail(email) != null){
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.cadastrar(superAdministrador, request);

            response.sendRedirect(
                    request.getContextPath() + "/superAdmin"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Exclui um super administrador pelo id da requisição
    private void deletarAdmin(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        int id = Parametros.inteiro(request, "id");

        try (SuperAdministradorDAO dao = new SuperAdministradorDAO()){

            // Não pode excluir a própria conta
            SuperAdminDTO logado = (SuperAdminDTO) request.getSession().getAttribute("usuario");
            if (logado != null && logado.getId() == id) {
                throw ExcecaoDeJSP.naoPodeExcluirPropriaConta();
            }

            // Não pode excluir o único administrador
            if (dao.buscar(null, null, null, null).size() <= 1) {
                throw ExcecaoDeJSP.naoPodeExcluirUltimoAdmin();
            }

            dao.remover(id, request);

            response.sendRedirect(
                    request.getContextPath() + "/superAdmin"
            );
        } catch (ExcecaoDeJSP e) {
            // Volta para a lista com o motivo do bloqueio
            request.setAttribute("erro", e.getMessage());
            listarAdmins(request, response);
            request.getRequestDispatcher(PAGINA_PRINCIPAL).forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Atualiza um super administrador com os dados do formulário
    private void atualizarAdmin(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Parametros.inteiro(request, "id");

        String nome = request.getParameter("nome");

        String email = request.getParameter("email");
        if (!RegexUtils.validarEmail(email)) {
            throw ExcecaoDeJSP.valorInvalido("email");
        }

        // Em branco vira null e mantém a senha atual
        String senha = Parametros.senhaOpcional(request, "senha");

        if (senha != null) {
            senha = HashSenha.gerarHash(senha);
        }

        try (SuperAdministradorDAO dao = new SuperAdministradorDAO()) {

            // Busca o registro atual no banco
            SuperAdministrador original = dao.pesquisarPorId(id);

            // Monta o objeto com os dados novos
            SuperAdministrador alterado = new SuperAdministrador(
                    nome,
                    senha,
                    email
            );

            SuperAdministrador superAdministradorApoioEmail = dao.pesquisarPorEmail(email);

            if (superAdministradorApoioEmail != null && superAdministradorApoioEmail.getId() != id){
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.atualizar(original, alterado, request);

            response.sendRedirect(
                    request.getContextPath() + "/superAdmin"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
