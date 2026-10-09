package com.servlet;

import com.DAO.SuperAdministradorDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Filtro;
import com.model.SuperAdministrador;
import com.model.enums.OperacaoFiltro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SuperAdministradorServlet", value = "/superAdmin")
public class SuperAdministradorServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null){
            action = "read";
        }

        if (action.equals("read")){
            listarAdmins(request, response);
        } else if (action.equals("create")) {

            request
                    .getRequestDispatcher("/WEB-INF/views/cadastro-superAdmin.jsp")
                    .forward(request, response);

        } else if (action.equals("update")) {

            int id = Integer.parseInt(
                    request.getParameter("id")
            );

            try (SuperAdministradorDAO dao = new SuperAdministradorDAO()) {

                SuperAdministrador superAdministrador = dao.pesquisarPorId(id);

                request.setAttribute("superAdmin", superAdministrador);

                request
                        .getRequestDispatcher("/WEB-INF/views/editar-superAdmin.jsp")
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
            cadastrarAdmin(request, response);
        } else if ("update".equals(action)) {
            atualizarAdmin(request, response);
        } else if ("delete".equals(action)){
            deletarAdmin(request, response);
        }
    }

    private void listarAdmins(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (SuperAdministradorDAO dao = new SuperAdministradorDAO()) {

            List<Filtro> filtros = new ArrayList<>();

            String[] campos = request.getParameterValues("campoFiltro");
            String[] valores = request.getParameterValues("valorFiltro");
            String[] operacoes = request.getParameterValues("operacaoFiltro");

            String campoSequencia;
            String direcaoSequencia;

            String ordenacao = request.getParameter("ordenacao");

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

            List<SuperAdministrador> superAdmins = dao.buscar(
                    filtros,
                    campoSequencia,
                    direcaoSequencia,
                    request.getParameter("pesquisa")
            );

            request.setAttribute("superAdmins", superAdmins);
            request.setAttribute("filtros", filtros);

            request
                    .getRequestDispatcher("/WEB-INF/views/superAdmin.jsp")
                    .forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void cadastrarAdmin(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String nome = request.getParameter("nome");

        String email = request.getParameter("email");

        String senha = request.getParameter("senha");


        SuperAdministrador superAdministrador = new SuperAdministrador(
                nome,
                senha,
                email
        );

        try (SuperAdministradorDAO dao = new SuperAdministradorDAO()) {

            if (dao.pesquisarPorEmail(email) != null){
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.cadastrar(superAdministrador);

            response.sendRedirect(
                    request.getContextPath() + "/superAdmin"
            );

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void deletarAdmin(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Integer.parseInt(request.getParameter("id"));

        try (SuperAdministradorDAO dao = new SuperAdministradorDAO()){
            dao.remover(id);

            response.sendRedirect(
                    request.getContextPath() + "/superAdmin"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void atualizarAdmin(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("id")
        );

        String nome = request.getParameter("nome");

        String email = request.getParameter("email");

        String senha = request.getParameter("senha");

        if (senha != null && senha.isBlank()) {
            senha = null;
        }

        try (SuperAdministradorDAO dao = new SuperAdministradorDAO()) {

            // Busca como está atualmente no banco
            SuperAdministrador original = dao.pesquisarPorId(id);

            // Monta o objeto com os novos dados
            SuperAdministrador alterado = new SuperAdministrador(
                    nome,
                    senha,
                    email
            );

            SuperAdministrador superAdministradorApoioEmail = dao.pesquisarPorEmail(email);

            if (superAdministradorApoioEmail != null && superAdministradorApoioEmail.getId() != id){
                throw ExcecaoDeJSP.emailDuplicado();
            }

            dao.atualizar(original, alterado);

            response.sendRedirect(
                    request.getContextPath() + "/superAdmin"
            );

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
}
