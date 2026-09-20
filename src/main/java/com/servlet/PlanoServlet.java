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

@WebServlet(name = "PlanoServlet", value = "/plano")
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
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
}
