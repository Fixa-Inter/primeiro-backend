package com.servlet;

import com.DAO.PlanoDAO;
import com.model.Plano;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
        } else if (action.equals("delete")){
            deletarPlano(request, response);
        }
    }

    private void listarPlanos(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (PlanoDAO dao = new PlanoDAO()) {

            String campoFiltro = request.getParameter("campoFiltro");
            String valorFiltroTexto = request.getParameter("valorFiltro");
            Object valorFiltro = null;
            String campoSequencia, direcaoSequencia;
            String ordenacao = request.getParameter("ordenacao");

            if (campoFiltro != null && !campoFiltro.isBlank()
                    && valorFiltroTexto != null && !valorFiltroTexto.isBlank()) {

                if (campoFiltro.equals("VALOR_MENSAL")) {

                    valorFiltro = Double.parseDouble(valorFiltroTexto);

                } else if (campoFiltro.equals("DURACAO_MESES")) {

                    valorFiltro = Integer.parseInt(valorFiltroTexto);

                } else {

                    valorFiltro = valorFiltroTexto;
                }
            }

            if (ordenacao != null && !ordenacao.isBlank()){
                campoSequencia = ordenacao.split("-")[0];
                direcaoSequencia = ordenacao.split("-")[1];
            } else{
                campoSequencia = null;
                direcaoSequencia = null;
            }

            ArrayList<Plano> planos = dao.buscar(
                    campoFiltro,
                    valorFiltro,
                    campoSequencia,
                    direcaoSequencia
            );

            request.setAttribute("planos", planos);

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

        Plano plano = new Plano(
                null,
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
                    id,
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
