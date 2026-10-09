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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null){
            action = "read";
        }

        if (action.equals("read")){
            listarPlanos(request, response);
        } else if (action.equals("create")) {

            request
                    .getRequestDispatcher("/WEB-INF/views/cadastro-plano.jsp")
                    .forward(request, response);

        } else if (action.equals("update")) {

            int id = Integer.parseInt(
                    request.getParameter("id")
            );

            try (PlanoDAO dao = new PlanoDAO()) {

                Plano plano = dao.pesquisarPorId(id);

                request.setAttribute("plano", plano);

                request
                        .getRequestDispatcher("/WEB-INF/views/editar-plano.jsp")
                        .forward(request, response);

            } catch (SQLException | ClassNotFoundException e) {
                throw new ServletException(e);
            }
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            cadastrarPlano(request, response);
        } else if ("update".equals(action)) {
            atualizarPlano(request, response);
        } else if ("delete".equals(action)){
            deletarPlano(request, response);
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

            request
                    .getRequestDispatcher("/WEB-INF/views/planos.jsp")
                    .forward(request, response);

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
