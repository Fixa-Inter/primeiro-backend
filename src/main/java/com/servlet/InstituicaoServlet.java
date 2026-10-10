package com.servlet;

import com.DAO.InstituicaoDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Filtro;
import com.model.Instituicao;
import com.model.enums.OperacaoFiltro;
import com.model.enums.TipoInstituicao;
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

@WebServlet(name = "InstituicaoServlet", value = "/instituicoes")
public class InstituicaoServlet extends HttpServlet{

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/instituicoes.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/cadastro-instituicao.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/editar-instituicao.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

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
                    request.setAttribute("instituicao", instituicao);
                }
                destino = PAGINA_EDICAO;
            }
            erro = false;
        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);
        } catch (ClassNotFoundException e) {
            System.err.println("Falha ao carregar o driver postgresql:");
            e.printStackTrace(System.err);
        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        if (erro) response.sendRedirect(request.getContextPath() + PAGINA_ERRO);
        else request.getRequestDispatcher(destino).forward(request, response);
    }

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
            }
        }
        catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
        }
    }

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
                    && !removerFiltroParam.isBlank()) {

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


                    String campo = campos[i];
                    String valor = valores[i];

                    OperacaoFiltro operacao =
                            OperacaoFiltro.valueOf(operacoes[i]);

                    Object valorConvertido =
                            dao.converterValor(campo, valor);

                    filtros.add(new Filtro(
                            campo,
                            valorConvertido,
                            operacao
                    ));
                }
            }

            if (ordenacao != null && !ordenacao.isBlank()) {

                String[] partesOrdenacao = ordenacao.split("-");

                campoSequencia = partesOrdenacao[0];
                direcaoSequencia = partesOrdenacao[1];

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

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void cadastrarInstituicao(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException, ExcecaoDeJSP{


        String nome = request.getParameter("nome");
        String emailCorporativo = request.getParameter("emailCorporativo");
        String dominioEmail = request.getParameter("dominioEmail");

        Integer tipoInstituicao = Integer.parseInt(
                request.getParameter("tipoInstituicao")
        );

        //verificações
        if (nome.isBlank()){
            throw ExcecaoDeJSP.notNullVazio("nome");
        }

        if (emailCorporativo.isBlank()){
            throw ExcecaoDeJSP.notNullVazio("email corporativo");
        }

        if (request.getParameter("tipoInstituicao").isBlank()){
            throw ExcecaoDeJSP.notNullVazio("tipo de instituicao");
        }

        Instituicao instituicao = new Instituicao(nome, emailCorporativo, TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail);

        try (InstituicaoDAO dao = new InstituicaoDAO()) {

            if (dao.pesquisarPorDominioEmail(dominioEmail) != null){
                throw ExcecaoDeJSP.dominioDuplicado();
            }

            if (dao.pesquisarPorEmailCorporativo(emailCorporativo) != null){
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.cadastrar(instituicao);

            response.sendRedirect(
                    request.getContextPath() + "/instituicoes"
            );

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void deletarInstituicao(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Integer.parseInt(request.getParameter("id"));

        try (InstituicaoDAO dao = new InstituicaoDAO()){
            dao.remover(id);

            response.sendRedirect(
                    request.getContextPath() + "/instituicoes"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void atualizarInstituicao(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException, ExcecaoDeJSP{

        int id = Integer.parseInt(
                request.getParameter("id")
        );

        String nome = request.getParameter("nome");
        String emailCorporativo = request.getParameter("emailCorporativo");

        String dominioEmail = request.getParameter("dominioEmail");

        Integer tipoInstituicao = Integer.parseInt(
                request.getParameter("tipoInstituicao")
        );

        try (InstituicaoDAO dao = new InstituicaoDAO()) {

            // Busca como está atualmente no banco
            Instituicao original = dao.pesquisarPorId(id);

            // Monta o objeto com os novos dados
            Instituicao alterado = new Instituicao(id, nome, emailCorporativo, original.getDataCadastro(), TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail);

            Instituicao instituicaoApoioDominio = dao.pesquisarPorDominioEmail(dominioEmail);
            Instituicao instituicaoApoioEmail = dao.pesquisarPorEmailCorporativo(emailCorporativo);

            if (instituicaoApoioDominio != null && instituicaoApoioDominio.getId() != id){
                throw ExcecaoDeJSP.dominioDuplicado();
            }

            if (instituicaoApoioEmail != null && instituicaoApoioEmail.getEmailCorporativo() != emailCorporativo){
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.atualizar(original, alterado);

            response.sendRedirect(
                    request.getContextPath() + "/instituicoes"
            );

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
}
