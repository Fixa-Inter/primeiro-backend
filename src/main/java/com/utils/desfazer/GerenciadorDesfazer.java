package com.utils.desfazer;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/DesfazerController")
public class GerenciadorDesfazer extends HttpServlet {
    private static final String CHAVE_SESSAO = "ULTIMO_COMANDO_DESFAZER_SISTEMA";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        desfazerUltimaAcao(request);

        String referer = request.getHeader("Referer");
        String contextPath = request.getContextPath();
        if (referer != null && referer.startsWith(request.getScheme() + "://" + request.getServerName())
                && referer.contains(contextPath + "/")) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(contextPath + "/area-restrita/index.jsp");
        }
    }

    /**
     * Salva o comando de reversão atual na sessão, matando o rascunho anterior.
     */
    public static void registrarComando(HttpServletRequest request, ComandoDesfazer comando) {
        request.getSession().setAttribute(CHAVE_SESSAO, comando);
    }

    /**
     * Acionado quando o usuário clica no botão "Desfazer / Cancelar última ação".
     */
    public static boolean desfazerUltimaAcao(HttpServletRequest request) {
        ComandoDesfazer comando = (ComandoDesfazer) request.getSession().getAttribute(CHAVE_SESSAO);

        if (comando != null) {
            try {
                comando.desfazer(); // Executa a lógica específica do comando gravado
                request.getSession().removeAttribute(CHAVE_SESSAO); // Limpa a memória RAM após desfazer
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false; // Não havia nada para desfazer
    }
}

