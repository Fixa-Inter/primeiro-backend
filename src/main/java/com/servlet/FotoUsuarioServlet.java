package com.servlet;

import com.DAO.FotoUsuarioDAO;
import com.DAO.SuperAdministradorDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Filtro;
import com.model.FotoUsuario;
import com.model.SuperAdministrador;
import com.model.enums.OperacaoFiltro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FotoUsuarioServlet", value = "/fotoUsuario")
public class FotoUsuarioServlet extends HttpServlet{

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/fotoUsuario.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/cadastro-fotoUsuario.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/editar-fotoUsuario.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        boolean erro = true;
        String destino = null;

        if (action == null) {
            action = "read";
        }
        try {
            if (action.equals("read")) {
                listarFotos(request, response);
                destino = PAGINA_PRINCIPAL;
            } else if (action.equals("create")) {
                destino = PAGINA_CADASTRO;
            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (FotoUsuarioDAO dao = new FotoUsuarioDAO()) {
                    FotoUsuario fotoUsuario = dao.pesquisarPorId(id);
                    request.setAttribute("fotoUsuario", fotoUsuario);
                }
                destino = PAGINA_EDICAO;
            }
            erro = false;
        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);
        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        if (erro) {
            response.sendRedirect(request.getContextPath() + PAGINA_ERRO);
        } else {
            request.getRequestDispatcher(destino).forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                cadastrarFotoUsuario(request, response);
            } else if ("update".equals(action)) {
                atualizarFotoUsuario(request, response);
            } else if ("delete".equals(action)){
                deletarFotoUsuario(request, response);
            }
        } catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
        }
    }

    private void listarFotos(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (FotoUsuarioDAO dao = new FotoUsuarioDAO()) {

            List<FotoUsuario> fotoUsuarios = dao.listar(request.getParameter("pesquisa"));

            request.setAttribute("fotos", fotoUsuarios);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void cadastrarFotoUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String fkUsuarioParam = request.getParameter("fkUsuario");
        String url = request.getParameter("url");

        int fkUsuario = Integer.parseInt(fkUsuarioParam);


        FotoUsuario fotoUsuario = new FotoUsuario(
                url,
                fkUsuario
        );

        try (FotoUsuarioDAO dao = new FotoUsuarioDAO()) {

            dao.cadastrar(fotoUsuario);

            response.sendRedirect(
                    request.getContextPath() + "/fotoUsuario"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void deletarFotoUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Integer.parseInt(request.getParameter("id"));

        try (FotoUsuarioDAO dao = new FotoUsuarioDAO()){
            dao.remover(id);

            response.sendRedirect(
                    request.getContextPath() + "/fotoUsuario"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void atualizarFotoUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("id")
        );

        String nome = request.getParameter("nome");

        String email = request.getParameter("email");


        try (FotoUsuarioDAO dao = new FotoUsuarioDAO()) {

            // Busca como está atualmente no banco
            FotoUsuario original = dao.pesquisarPorId(id);

            // Monta o objeto com os novos dados
            int fkUsuario = Integer.parseInt(request.getParameter("fkUsuario"));

            String url = request.getParameter("url");


            FotoUsuario alterado = new FotoUsuario(
                    id,
                    original.getDataRegistro(),
                    url,
                    fkUsuario
            );

            dao.atualizar(original, alterado);

            response.sendRedirect(
                    request.getContextPath() + "/fotoUsuario"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
