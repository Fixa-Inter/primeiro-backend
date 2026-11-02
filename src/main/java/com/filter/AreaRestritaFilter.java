package com.filter;

import com.DTO.SuperAdminDTO;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Bloqueia o acesso à área restrita sem login
@WebFilter(
        filterName = "area-restrita-filter",
        urlPatterns = {
                "/area-restrita/*",
                "/instituicoes",
                "/planos",
                "/superAdmin",
                "/enderecos",
                "/contratos",
                "/usuarios",
                "/pagamentos",
                "/fotoUsuario",
                "/analiticos",
                "/DesfazerController"
        },
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD}
)
public class AreaRestritaFilter extends HttpFilter {

    // Deixa passar quem está logado e responde 401 para os demais
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        // Sem usuário logado, responde 401
        if (session != null && session.getAttribute("usuario") instanceof SuperAdminDTO) {
            chain.doFilter(req, resp);

        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
