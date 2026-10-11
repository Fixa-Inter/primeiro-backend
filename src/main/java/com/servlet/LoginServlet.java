package com.servlet;

import com.DAO.LoginDAO;
import com.DTO.LoginDTO;
import com.DTO.SuperAdminDTO;
import com.exception.ExcecaoDeJSP;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login-handler")
public class LoginServlet extends HttpServlet {

    private static final String AREA_RESTRITA = "/area-restrita/index.jsp";
    private static final String PAGINA_LOGIN = "jsp/login.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(PAGINA_LOGIN);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action").trim();
        HttpSession session = req.getSession();

        String destino = PAGINA_ERRO;

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

                default -> throw new RuntimeException("valor inválido para o parâmetro 'action': " + action);
            }

            destino = AREA_RESTRITA;

        }
        // Se houver alguma exceção de JSP, aciona o método doGet
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

        resp.sendRedirect(req.getContextPath() + destino);
    }

    // === LOGIN ===
    private SuperAdminDTO login(HttpServletRequest req) throws SQLException, ClassNotFoundException, ExcecaoDeJSP {
        String email = req.getParameter("email").trim();
        String senha = req.getParameter("senha").trim();
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
