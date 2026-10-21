package com.filter;

import com.DTO.SuperAdminDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Servlet para filtrar o acesso a páginas JSP da área-restrita, uma vez que não estamos utilizando WEB-INF

@WebFilter(filterName = "area-restrita-filter", urlPatterns = "/area-restrita/*")
public class AreaRestritaFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        // Não tem usuário, redireciona para página de login
        if (session != null && session.getAttribute("usuario") instanceof SuperAdminDTO) {
            chain.doFilter(req, resp);

        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
