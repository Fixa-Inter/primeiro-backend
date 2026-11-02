package com.servlet;

import com.DAO.FotoUsuarioDAO;
import com.DAO.SuperAdministradorDAO;
import com.DTO.FotoUsuarioDTO;
import com.exception.ExcecaoDeJSP;
import com.utils.Parametros;
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

// Controla o CRUD de fotos de usuário na área restrita
@WebServlet(name = "FotoUsuarioServlet", value = "/fotoUsuario")
public class FotoUsuarioServlet extends HttpServlet{

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/restrita/foto-usuario.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/restrita/cadastro-foto-usuario.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/restrita/editar-foto-usuario.jsp";

    // Abre a lista, o formulário de cadastro ou o de edição, conforme a action
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
                try (com.DAO.UsuarioDAO usuarioDAO = new com.DAO.UsuarioDAO()) {
                    request.setAttribute("usuariosDisponiveis", usuarioDAO.listar(null, null, null, null));
                }
                destino = PAGINA_CADASTRO;
            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (FotoUsuarioDAO dao = new FotoUsuarioDAO()) {
                    FotoUsuario fotoUsuario = dao.pesquisarPorId(id);
                    if (fotoUsuario == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    request.setAttribute("fotoUsuario", fotoUsuario);
                    request.setAttribute("fotoUsuarioDTO", dao.listarFotoUsuarioDTO(null).stream()
                            .filter(dto -> dto.getId().equals(id)).findFirst().orElse(null));
                }
                try (com.DAO.UsuarioDAO usuarioDAO = new com.DAO.UsuarioDAO()) {
                    request.setAttribute("usuariosDisponiveis", usuarioDAO.listar(null, null, null, null));
                }
                destino = PAGINA_EDICAO;
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            erro = false;
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);
        } catch (Throwable e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace(System.err);
        }

        if (erro) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            request.getRequestDispatcher(destino).forward(request, response);
        }
    }

    // Executa o cadastro, a edição ou a exclusão, conforme a action
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
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
        }
    }

    // Lista as fotos de usuário com a pesquisa da requisição
    private void listarFotos(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (FotoUsuarioDAO dao = new FotoUsuarioDAO()) {

            List<FotoUsuarioDTO> fotoUsuarios = dao.listarFotoUsuarioDTO(request.getParameter("pesquisa"));

            request.setAttribute("fotos", fotoUsuarios);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Cadastra uma foto de usuário com os dados do formulário
    private void cadastrarFotoUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String url = Parametros.obrigatorio(request, "url");

        int fkUsuario = Parametros.inteiro(request, "fkUsuario");

        FotoUsuario fotoUsuario = new FotoUsuario(
                url,
                fkUsuario
        );

        try (FotoUsuarioDAO dao = new FotoUsuarioDAO()) {

            dao.cadastrar(fotoUsuario, request);

            response.sendRedirect(
                    request.getContextPath() + "/fotoUsuario"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Exclui uma foto de usuário pelo id da requisição
    private void deletarFotoUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Parametros.inteiro(request, "id");

        try (FotoUsuarioDAO dao = new FotoUsuarioDAO()){
            dao.remover(id, request);

            response.sendRedirect(
                    request.getContextPath() + "/fotoUsuario"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Atualiza uma foto de usuário com os dados do formulário
    private void atualizarFotoUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Parametros.inteiro(request, "id");

        try (FotoUsuarioDAO dao = new FotoUsuarioDAO()) {

            // Busca o registro atual no banco
            FotoUsuario original = dao.pesquisarPorId(id);

            // Monta o objeto com os dados novos
            int fkUsuario = Parametros.inteiro(request, "fkUsuario");

            String url = request.getParameter("url");

            FotoUsuario alterado = new FotoUsuario(
                    id,
                    original.getDataRegistro(),
                    url,
                    fkUsuario
            );

            dao.atualizar(original, alterado, request);

            response.sendRedirect(
                    request.getContextPath() + "/fotoUsuario"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
