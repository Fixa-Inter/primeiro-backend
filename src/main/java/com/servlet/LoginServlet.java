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

// Login e logout da área restrita
@WebServlet({"/login-handler", "/login"})
public class LoginServlet extends HttpServlet {

    // Rota do servlet, pois o redirect não alcança arquivos dentro de WEB-INF
    private static final String AREA_RESTRITA = "/analiticos";
    private static final String PAGINA_LOGIN = "/WEB-INF/views/publico/login.jsp";

    // Abre a tela de login
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(PAGINA_LOGIN);
        dispatcher.forward(req, resp);
    }

    // Executa o login ou o logout, conforme a action
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action");

        // Sem action o pedido é inválido (400)
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
        // Em caso de erro de JSP, reabre a tela com a mensagem
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

    // Valida as credenciais e devolve o super administrador logado
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

            // Login inválido
            if (usuario == null) {
                throw ExcecaoDeJSP.falhaLogin();
            }

            return usuario;
        }
    }

    // Remove o usuário da sessão
    private void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        // Encerra a sessão do usuário
        if (session != null) {
            session.removeAttribute("usuario");
        }
    }
}
