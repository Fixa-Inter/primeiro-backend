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
                    .getRequestDispatcher("/WEB-INF/views/cadastro-planos.jsp")
                    .forward(request, response);

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
        } else if (action.equals("delete")){
            deletarPlano(request, response);
        }
    }

    private void listarPlanos(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (PlanoDAO dao = new PlanoDAO()) {

            ArrayList<Plano> planos = dao.buscar(
                    null,
                    null,
                    null,
                    null
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
}
