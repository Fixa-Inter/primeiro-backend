package com.servlet;

import com.DAO.EnderecoDAO;
import com.DAO.UsuarioDAO;
import com.DTO.UsuarioDTO;
import com.exception.ExcecaoDeJSP;
import com.model.*;
import com.model.enums.OperacaoFiltro;
import com.model.enums.TipoAcesso;
import com.service.HashSenha;
import com.utils.Parametros;
import com.utils.RegexUtils;
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

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/restrita/usuarios.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/restrita/cadastro-usuario.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/restrita/editar-usuario.jsp";

    @Override
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
                listarUsuarios(request, response);
                destino = PAGINA_PRINCIPAL;
            } else if (action.equals("create")) {
                try (EnderecoDAO enderecoDAO = new EnderecoDAO()) {
                    request.setAttribute("enderecosDisponiveis", enderecoDAO.listar(null, null, null, null));
                }
                destino = PAGINA_CADASTRO;
            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (UsuarioDAO dao = new UsuarioDAO()) {
                    Usuario usuario = dao.pesquisarPorId(id);
                    if (usuario == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    request.setAttribute("usuario", usuario);
                    request.setAttribute("usuarioDTO", dao.listarUsuariosDTO(null, null, null, null).stream()
                            .filter(dto -> dto.getId().equals(id)).findFirst().orElse(null));
                }
                // opcoes do menu de endereco da tela de edicao (igual ao cadastro)
                try (EnderecoDAO enderecoDAO = new EnderecoDAO()) {
                    request.setAttribute("enderecosDisponiveis", enderecoDAO.listar(null, null, null, null));
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

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                cadastrarUsuario(request, response);
            } else if ("update".equals(action)) {
                atualizarUsuario(request, response);
            } else if ("delete".equals(action)){
                deletarUsuario(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
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

            List<UsuarioDTO> usuarios = dao.listarUsuariosDTO(
                    filtros,
                    campoSequencia,
                    direcaoSequencia,
                    pesquisa
            );

            request.setAttribute("usuarios",
                    usuarios);
            request.setAttribute("filtros", filtros);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void cadastrarUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String nome = Parametros.obrigatorio(request, "nome");

        String senha = Parametros.obrigatorio(request, "senha");

        String email = request.getParameter("email");

        if (!RegexUtils.validarEmail(email)) {
            throw ExcecaoDeJSP.valorInvalido("email");
        }

        String cargo = request.getParameter("cargo");

        // o formulario envia o codigo do tipo de acesso (1 a 4)
        TipoAcesso tipoDeAcesso = TipoAcesso.converterEnum(
                Parametros.inteiro(request, "tipoDeAcesso")
        );

        int fkEndereco = Parametros.inteiro(request, "fkEndereco");

        LocalDate dataAniversario = Parametros.data(request, "dataAniversario");

        Usuario usuario = new Usuario(
                nome,
                HashSenha.gerarHash(senha),
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

            dao.cadastrar(usuario, request);

            response.sendRedirect(
                    request.getContextPath() + "/usuarios"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void deletarUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Parametros.inteiro(request, "id");

        try (UsuarioDAO usuarioDAO = new UsuarioDAO()){
            usuarioDAO.remover(id, request);

            response.sendRedirect(
                    request.getContextPath() + "/usuarios"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void atualizarUsuario(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Parametros.inteiro(request, "id");

        String nome = request.getParameter("nome");

        String senha = request.getParameter("senha");

        boolean estaAtivo = Parametros.booleano(request, "estaAtivo");

        String email = request.getParameter("email");

        if (!RegexUtils.validarEmail(email)) {
            throw ExcecaoDeJSP.valorInvalido("email");
        }

        String cargo = request.getParameter("cargo");

        // o formulario envia o codigo do tipo de acesso (1 a 4)
        TipoAcesso tipoDeAcesso = TipoAcesso.converterEnum(
                Parametros.inteiro(request, "tipoDeAcesso")
        );

        int fkEndereco = Parametros.inteiro(request, "fkEndereco");

        LocalDate dataAniversario = Parametros.dataOpcional(request, "dataAniversario");

        boolean primeiroAcesso = Parametros.booleano(request, "primeiroAcesso");

        try (UsuarioDAO dao = new UsuarioDAO()) {

            // Busca como está atualmente no banco
            Usuario original = dao.pesquisarPorId(id);
            String senhaHash = senha == null || senha.isBlank()
                    ? original.getSenhaHash()
                    : HashSenha.gerarHash(senha);

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

            dao.atualizar(original, alterado, request);

            response.sendRedirect(
                    request.getContextPath() + "/usuarios"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

}
