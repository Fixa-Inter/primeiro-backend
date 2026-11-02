package com.utils.desfazer;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Guarda e executa o desfazer da última ação
@WebServlet("/DesfazerController")
public class GerenciadorDesfazer extends HttpServlet {
    private static final String CHAVE_SESSAO = "ULTIMO_COMANDO_DESFAZER_SISTEMA";

    // Desfaz a última ação e volta para a página anterior
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
            response.sendRedirect(contextPath + "/analiticos");
        }
    }

    // Guarda o comando de reversão na sessão, substituindo o anterior
    public static void registrarComando(HttpServletRequest request, ComandoDesfazer comando) {
        request.getSession().setAttribute(CHAVE_SESSAO, comando);

        // Guarda o aviso da próxima lista, com o botão Desfazer
        String mensagem;
        if (comando instanceof DesfazerInsert) {
            mensagem = "Registro criado com sucesso.";
        } else if (comando instanceof DesfazerUpdate) {
            mensagem = "Registro editado com sucesso.";
        } else {
            mensagem = "Registro excluído com sucesso.";
        }
        guardarAviso(request, mensagem, true);
    }

    // Guarda na sessão o aviso exibido pelo _aviso.jspf na próxima lista
    private static void guardarAviso(HttpServletRequest request, String mensagem, boolean comDesfazer) {
        request.getSession().setAttribute("AVISO_TEXTO", mensagem);
        request.getSession().setAttribute("AVISO_COM_DESFAZER", comDesfazer);
    }

    // Executa o comando guardado e devolve se conseguiu desfazer
    public static boolean desfazerUltimaAcao(HttpServletRequest request) {
        ComandoDesfazer comando = (ComandoDesfazer) request.getSession().getAttribute(CHAVE_SESSAO);

        if (comando != null) {
            try {
                comando.desfazer();  // Executa o comando gravado
                request.getSession().removeAttribute(CHAVE_SESSAO);  // Limpa o comando da sessão
                guardarAviso(request, "Ação desfeita.", false);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;  // Nada para desfazer
    }
}

