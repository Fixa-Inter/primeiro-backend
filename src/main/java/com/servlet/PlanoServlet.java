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

        try {
            PlanoDAO planoDAO = new PlanoDAO();
            ArrayList<Plano> planos = planoDAO.buscar(null,null,null,null);

            request.setAttribute("planos", planos);

            request
                    .getRequestDispatcher("/WEB-INF/views/planos.jsp")
                    .forward(request, response);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
