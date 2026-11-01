package com.servlet;

import com.DAO.LoginDAO;
import com.DTO.LoginDTO;
import com.DTO.SuperAdminDTO;
import com.exception.ExcecaoDeJSP;
import com.utils.RegexUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet({"/login-handler", "/login"})
public class LoginServlet extends HttpServlet {

    // rota do servlet: o redirect do navegador nao alcanca arquivos dentro de WEB-INF
    private static final String AREA_RESTRITA = "/analiticos";
    private static final String PAGINA_LOGIN = "/WEB-INF/views/publico/login.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("usuario") != null) {
            // Redireciona direto para a área restrita (analiticos)
            resp.sendRedirect(req.getContextPath() + AREA_RESTRITA);
            return; // Encerra a execução do método aqui
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher(PAGINA_LOGIN);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action");

        // sem action o pedido esta mal formado (400), em vez de estourar NullPointerException
        if (action == null || action.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        action = action.trim();

        HttpSession session = req.getSession();

        String destino = null;

        try {
            switch (action) {
                case "login" -> {
                    SuperAdminDTO usuario = login(req);
                    session.setAttribute("usuario", usuario);
                }

                case "logout" -> {
                    logout(req);
                    doGet(req, resp);
                    return;
                }

                default -> {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }

            destino = AREA_RESTRITA;

        }
        // Se houver alguma exceção de JSP, aciona o metodo doGet
        catch (ExcecaoDeJSP e) {
            req.setAttribute("erro", e.getMessage());
            doGet(req, resp);
            return;

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

        if (destino != null) {
            resp.sendRedirect(req.getContextPath() + destino);
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // === LOGIN ===
    private SuperAdminDTO login(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
        String email = req.getParameter("email");
        if (!RegexUtils.validarEmail(email)) {
            throw ExcecaoDeJSP.valorInvalido("email");
        }
        email = email.trim();
        String senhaParam = req.getParameter("senha");
        if (senhaParam == null || senhaParam.isBlank()) {
            throw ExcecaoDeJSP.notNullVazio("senha");
        }
        String senha = senhaParam.trim();
        LoginDTO credenciais = new LoginDTO(email, senha);

        try (LoginDAO dao = new LoginDAO()) {
            SuperAdminDTO usuario = dao.login(credenciais);

            // Validação de login
            if (usuario == null) {
                throw ExcecaoDeJSP.falhaLogin();
            }

            return usuario;
        }
    }

    // === LOGOUT ===
    private void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        // Finaliza a sessão do usuário
        if (session != null) {
            session.removeAttribute("usuario");
        }
    }
}
