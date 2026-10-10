package com.servlet;

import com.DAO.PlanoDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Filtro;
import com.model.Plano;
import com.model.enums.OperacaoFiltro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PlanoServlet", value = "/planos")
public class PlanoServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/planos.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/cadastro-plano.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/editar-plano.jsp";
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
                listarPlanos(request, response);
                destino = PAGINA_PRINCIPAL;
            } else if (action.equals("create")) {
                destino = PAGINA_CADASTRO;
            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PlanoDAO dao = new PlanoDAO()) {
                    Plano plano = dao.pesquisarPorId(id);
                    request.setAttribute("plano", plano);
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
    ) throws ServletException, IOException {

        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                cadastrarPlano(request, response);
            } else if ("update".equals(action)) {
                atualizarPlano(request, response);
            } else if ("delete".equals(action)) {
                deletarPlano(request, response);
            }
        }catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
        }
    }

    private void listarPlanos(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (PlanoDAO dao = new PlanoDAO()) {

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

            List<Plano> planos = dao.buscar(
                    filtros,
                    campoSequencia,
                    direcaoSequencia,
                    pesquisa
            );

            request.setAttribute("planos", planos);
            request.setAttribute("filtros", filtros);

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void cadastrarPlano(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String nome = request.getParameter("nome");

        double valor = Double.parseDouble(
                request.getParameter("valor")
        );

        int duracao = Integer.parseInt(
                request.getParameter("duracao")
        );

        String descricao = request.getParameter("descricao");

        //verificacoes necessarias
        if (request.getParameter("valor").isBlank()){
            throw ExcecaoDeJSP.notNullVazio("valor");
        }

        if (request.getParameter("duracao").isBlank()){
            throw ExcecaoDeJSP.notNullVazio("duracao");
        }

        Plano plano = new Plano(
                nome,
                valor,
                duracao,
                descricao
        );

        try (PlanoDAO dao = new PlanoDAO()) {

            dao.cadastrar(plano);

            response.sendRedirect(
                    request.getContextPath() + "/planos"
            );

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void deletarPlano(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Integer.parseInt(request.getParameter("id"));

        try (PlanoDAO planoDAO = new PlanoDAO()){
            planoDAO.remover(id);

            response.sendRedirect(
                    request.getContextPath() + "/planos"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void atualizarPlano(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("id")
        );

        String nome = request.getParameter("nome");

        double valorMensal = Double.parseDouble(
                request.getParameter("valorMensal")
        );

        int duracaoMeses = Integer.parseInt(
                request.getParameter("duracaoMeses")
        );

        String descricao = request.getParameter("descricao");



        try (PlanoDAO dao = new PlanoDAO()) {

            // Busca como está atualmente no banco
            Plano original = dao.pesquisarPorId(id);

            // Monta o objeto com os novos dados
            Plano alterado = new Plano(
                    nome,
                    valorMensal,
                    duracaoMeses,
                    descricao
            );

            dao.atualizar(original, alterado);

            response.sendRedirect(
                    request.getContextPath() + "/planos"
            );

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
}
