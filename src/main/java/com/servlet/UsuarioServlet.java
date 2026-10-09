package com.servlet;

import com.DAO.UsuarioDAO;
import com.exception.ExcecaoDeJSP;
import com.model.*;
import com.model.enums.OperacaoFiltro;
import com.model.enums.TipoAcesso;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UsuarioServlet", value = "/usuarios")
public class UsuarioServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null){
            action = "read";
        }

        if (action.equals("read")){
            listarUsuarios(request, response);
        } else if (action.equals("create")) {

            request
                    .getRequestDispatcher("/WEB-INF/views/cadastro-usuario.jsp")
                    .forward(request, response);

        } else if (action.equals("update")) {

            int id = Integer.parseInt(
                    request.getParameter("id")
            );

            try (UsuarioDAO dao = new UsuarioDAO()) {

                Usuario usuario = dao.pesquisarPorId(id);

                request.setAttribute("usuario", usuario);

                request
                        .getRequestDispatcher("/WEB-INF/views/editar-usuario.jsp")
                        .forward(request, response);

            } catch (SQLException | ClassNotFoundException e) {
                throw new ServletException(e);
            }
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            cadastrarUsuario(request, response);
        } else if ("update".equals(action)) {
            atualizarUsuario(request, response);
        } else if ("delete".equals(action)){
            deletarUsuario(request, response);
        }
    }

    private void listarUsuarios(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (UsuarioDAO dao = new UsuarioDAO()) {

            List<Filtro> filtros = new ArrayList<>();

            String[] campos = request.getParameterValues("campoFiltro");
            String[] valores = request.getParameterValues("valorFiltro");
            String[] operacoes = request.getParameterValues("operacaoFiltro");

            String campoSequencia;
            String direcaoSequencia;

            String ordenacao = request.getParameter("ordenacao");

            String pesquisa = request.getParameter("pesquisa");

            String removerFiltroParam = request.getParameter("removerFiltro");
            Integer indiceRemover = null;
            if (removerFiltroParam != null
                    && !removerFiltroParam.isBlank()) {

                indiceRemover =
                        Integer.parseInt(removerFiltroParam);
            }

            if (campos != null
                    && valores != null
                    && operacoes != null
                    && campos.length == valores.length
                    && campos.length == operacoes.length) {

                for (int i = 0; i < campos.length; i++) {

                    if (indiceRemover != null
                            && i == indiceRemover) {

                        continue;
                    }


                    String campo = campos[i];
                    String valor = valores[i];

                    OperacaoFiltro operacao =
                            OperacaoFiltro.valueOf(operacoes[i]);

                    Object valorConvertido =
                            dao.converterValor(campo, valor);

                    filtros.add(new Filtro(
                            campo,
                            valorConvertido,
                            operacao
                    ));
                }
            }

            if (ordenacao != null && !ordenacao.isBlank()) {

                String[] partesOrdenacao = ordenacao.split("-");

                campoSequencia = partesOrdenacao[0];
                direcaoSequencia = partesOrdenacao[1];

            } else {

                campoSequencia = null;
                direcaoSequencia = null;
            }

            List<Usuario> usuarios = dao.listar(
                    filtros,
                    campoSequencia,
                    direcaoSequencia,
                    pesquisa
            );

            request.setAttribute("usuarios",
                    usuarios);
            request.setAttribute("filtros", filtros);

            request
                    .getRequestDispatcher("/WEB-INF/views/usuarios.jsp")
                    .forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void cadastrarUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String nome = request.getParameter("nome");

        String senha = request.getParameter("senha");

        String email = request.getParameter("email");

        String cargo = request.getParameter("cargo");

        TipoAcesso tipoDeAcesso = TipoAcesso.converterEnum(
                request.getParameter("tipoDeAcesso")
        );

        int fkEndereco = Integer.parseInt(
                request.getParameter("fkEndereco")
        );

        LocalDate dataAniversario = LocalDate.parse(
                request.getParameter("dataAniversario")
        );

        //verificacoes necessarias
        if (email.isBlank()){
            throw ExcecaoDeJSP.notNullVazio("email");
        }

        if (senha.isBlank()){
            throw ExcecaoDeJSP.notNullVazio("senha");
        }

        if (nome.isBlank()){
            throw ExcecaoDeJSP.notNullVazio("nome");
        }

        if (request.getParameter("tipoDeAcesso").isBlank()){
            throw ExcecaoDeJSP.notNullVazio("tipoDeAcesso");
        }

        Usuario usuario = new Usuario(
                nome,
                senha,
                email,
                cargo,
                tipoDeAcesso,
                fkEndereco,
                dataAniversario
        );

        try (UsuarioDAO dao = new UsuarioDAO()) {

            if (dao.pesquisarPorEmail(email) != null){
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.cadastrar(usuario);

            response.sendRedirect(
                    request.getContextPath() + "/usuarios"
            );

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void deletarUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Integer.parseInt(request.getParameter("id"));

        try (UsuarioDAO usuarioDAO = new UsuarioDAO()){
            usuarioDAO.remover(id);

            response.sendRedirect(
                    request.getContextPath() + "/usuarios"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void atualizarUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("id")
        );

        String nome = request.getParameter("nome");

        String senhaHash = request.getParameter("senha");

        boolean estaAtivo = Boolean.parseBoolean(
                request.getParameter("estaAtivo")
        );

        String email = request.getParameter("email");

        String cargo = request.getParameter("cargo");

        TipoAcesso tipoDeAcesso = TipoAcesso.converterEnum(
                request.getParameter("tipoDeAcesso")
        );

        int fkEndereco = Integer.parseInt(
                request.getParameter("fkEndereco")
        );

        String dataAniversarioParam = request.getParameter("dataAniversario");

        LocalDate dataAniversario = null;

        if (dataAniversarioParam != null && !dataAniversarioParam.isBlank()) {
            dataAniversario = LocalDate.parse(dataAniversarioParam);
        }

        boolean primeiroAcesso = Boolean.parseBoolean(
                request.getParameter("primeiroAcesso")
        );

        try (UsuarioDAO dao = new UsuarioDAO()) {

            // Busca como está atualmente no banco
            Usuario original = dao.pesquisarPorId(id);

            // Monta o objeto com os novos dados
            Usuario alterado = new Usuario(
                    id,
                    nome,
                    senhaHash,
                    estaAtivo,
                    email,
                    original.getDataCriacao(),
                    cargo,
                    tipoDeAcesso,
                    fkEndereco,
                    dataAniversario,
                    primeiroAcesso
            );

            Usuario usuarioApoioEmail = dao.pesquisarPorEmail(email);

            if (usuarioApoioEmail != null && usuarioApoioEmail.getId() != id){
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.atualizar(original, alterado);

            response.sendRedirect(
                    request.getContextPath() + "/usuarios"
            );

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

}
