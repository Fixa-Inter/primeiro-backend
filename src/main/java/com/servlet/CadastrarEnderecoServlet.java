package com.servlet;

import com.DAO.EnderecoDAO;
import com.DAO.InstituicaoDAO;
import com.DTO.EnderecoViaCnpjDTO;
import com.DTO.InstituicaoLogadaDTO;
import com.model.Endereco;
import com.model.Instituicao;
import com.service.ConsultaCnpjService;
import com.service.HashSenha;
import com.utils.RegexUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

// fluxo 2: instituicao ja cadastrada faz login e adiciona mais um endereco ligado ao seu id
@WebServlet(name = "CadastrarEnderecoServlet", value = "/cadastrar-endereco")
public class CadastrarEnderecoServlet extends HttpServlet {

    private static final String PAGINA_LOGIN = "/WEB-INF/views/publico/cadastrar-endereco/acesso-endereco.jsp";
    private static final String PAGINA_INFO = "/WEB-INF/views/publico/cadastrar-endereco/endereco-info.jsp";
    private static final String PAGINA_ENDERECO = "/WEB-INF/views/publico/cadastrar-endereco/adicionar-endereco.jsp";
    private static final String PAGINA_CONCLUIDO = "/WEB-INF/views/publico/cadastrar-endereco/endereco-concluido.jsp";

    // nome do atributo da sessao que guarda a instituicao logada
    private static final String SESSAO_INSTITUICAO = "instituicaoLogada";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "login";
        }

        if (action.equals("login")) {

            // se ja esta logada, pula direto para a tela de endereco
            HttpSession session = request.getSession(false);

            if (session != null && session.getAttribute(SESSAO_INSTITUICAO) != null) {
                response.sendRedirect(request.getContextPath() + "/cadastrar-endereco?action=info");
                return;
            }

            request
                    .getRequestDispatcher(PAGINA_LOGIN)
                    .forward(request, response);

        } else if (action.equals("info")) {

            HttpSession session = request.getSession(false);

            // sem login, volta para a tela de login
            if (session == null || session.getAttribute(SESSAO_INSTITUICAO) == null) {
                response.sendRedirect(request.getContextPath() + "/cadastrar-endereco");
                return;
            }

            request
                    .getRequestDispatcher(PAGINA_INFO)
                    .forward(request, response);

        } else if (action.equals("adicionar")) {

            HttpSession session = request.getSession(false);

            // sem login, volta para a tela de login
            if (session == null || session.getAttribute(SESSAO_INSTITUICAO) == null) {
                response.sendRedirect(request.getContextPath() + "/cadastrar-endereco");
                return;
            }

            request
                    .getRequestDispatcher(PAGINA_ENDERECO)
                    .forward(request, response);

        } else if (action.equals("concluido")) {

            HttpSession session = request.getSession(false);

            // sem login, volta para a tela de login
            if (session == null || session.getAttribute(SESSAO_INSTITUICAO) == null) {
                response.sendRedirect(request.getContextPath() + "/cadastrar-endereco");
                return;
            }

            request
                    .getRequestDispatcher(PAGINA_CONCLUIDO)
                    .forward(request, response);

        } else if (action.equals("logout")) {

            HttpSession session = request.getSession(false);

            if (session != null) {
                session.removeAttribute(SESSAO_INSTITUICAO);
            }

            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("login".equals(action)) {
            login(request, response);
        } else if ("buscarCnpj".equals(action)) {
            buscarCnpj(request, response);
        } else if ("finalizar".equals(action)) {
            finalizar(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = parametro(request, "email");
        String senha = request.getParameter("senha");

        if (email.isEmpty() || senha == null || senha.isEmpty()) {
            mostrarErro(request, response, PAGINA_LOGIN, "Informe o e-mail e a senha");
            return;
        }

        try (InstituicaoDAO instituicaoDAO = new InstituicaoDAO()) {

            Instituicao instituicao = instituicaoDAO.pesquisarPorEmailCorporativo(email);

            // mesma mensagem para email inexistente e senha errada, para nao revelar quais emails estao cadastrados
            if (instituicao == null || !HashSenha.conferir(senha, instituicao.getSenhaHash())) {
                mostrarErro(request, response, PAGINA_LOGIN, "E-mail ou senha inválidos");
                return;
            }

            // gera um novo id de sessao no login, para nao reaproveitar uma sessao anterior
            HttpSession session = request.getSession();
            request.changeSessionId();

            session.setAttribute(SESSAO_INSTITUICAO, new InstituicaoLogadaDTO(
                    instituicao.getId(),
                    instituicao.getNome(),
                    instituicao.getEmailCorporativo()
            ));

            response.sendRedirect(request.getContextPath() + "/cadastrar-endereco?action=info");

        } catch (SQLException e) {
            System.out.print("Erro de SQL ao fazer login da instituicao: " + e.getMessage());
            mostrarErro(request, response, PAGINA_LOGIN, "Erro ao acessar o sistema, tente novamente");
        }
    }

    private void buscarCnpj(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // sem login, volta para a tela de login
        if (session == null || session.getAttribute(SESSAO_INSTITUICAO) == null) {
            response.sendRedirect(request.getContextPath() + "/cadastrar-endereco");
            return;
        }

        String cnpj = parametro(request, "cnpj");

        if (!RegexUtils.validarCnpj(cnpj)) {
            request.setAttribute("cnpj", cnpj);
            mostrarErro(request, response, PAGINA_ENDERECO, "CNPJ inválido");
            return;
        }

        String cnpjFormatado = cnpj.replaceAll("\\D", "");

        try (EnderecoDAO enderecoDAO = new EnderecoDAO()) {

            // verifica se esse cnpj ja esta cadastrado, e se estiver da erro
            if (enderecoDAO.pesquisarPorCnpj(cnpjFormatado) != null) {
                request.setAttribute("cnpj", cnpj);
                mostrarErro(request, response, PAGINA_ENDERECO, "Esse CNPJ já está cadastrado");
                return;
            }

            // consulta a api
            EnderecoViaCnpjDTO dados = new ConsultaCnpjService().consultar(cnpjFormatado);

            if (dados == null) {
                request.setAttribute("cnpj", cnpj);
                mostrarErro(request, response, PAGINA_ENDERECO, "CNPJ inválido ou não encontrado");
                return;
            }

            // a jsp usa "etapa" para saber se mostra o passo 1 (buscar) ou o passo 2 (confirmar)
            request.setAttribute("etapa", "confirmar");
            request.setAttribute("cnpj", cnpjFormatado);
            request.setAttribute("rua", dados.getRua());
            request.setAttribute("bairro", dados.getBairro());
            request.setAttribute("cidade", dados.getCidade());
            request.setAttribute("estado", dados.getEstado());
            request.setAttribute("numero", dados.getNumero());
            request.setAttribute("complemento", dados.getComplemento());
            request.setAttribute("cep", dados.getCep());
            request.setAttribute("razaoSocial", dados.getRazaoSocial());

            request
                    .getRequestDispatcher(PAGINA_ENDERECO)
                    .forward(request, response);

        } catch (SQLException e) {
            System.out.print("Erro de SQL ao consultar cnpj no fluxo de endereco: " + e.getMessage());
            mostrarErro(request, response, PAGINA_ENDERECO, "Erro ao acessar o sistema, tente novamente");
        }
    }

    private void finalizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // o id da instituicao vem sempre da sessao, nunca do formulario
        InstituicaoLogadaDTO instituicao = (session == null) ? null : (InstituicaoLogadaDTO) session.getAttribute(SESSAO_INSTITUICAO);

        // sem login, volta para a tela de login
        if (instituicao == null) {
            response.sendRedirect(request.getContextPath() + "/cadastrar-endereco");
            return;
        }

        // o valor como foi digitado e usado na validacao do formato, e o so com digitos vai para o banco
        String cnpjInformado = parametro(request, "cnpj");
        String cepInformado = parametro(request, "cep");
        String cnpj = cnpjInformado.replaceAll("\\D", "");
        String cep = cepInformado.replaceAll("\\D", "");
        String nomeEndereco = parametro(request, "nomeEndereco");
        String rua = parametro(request, "rua");
        String bairro = parametro(request, "bairro");
        String complemento = parametro(request, "complemento");
        String cidade = parametro(request, "cidade");
        String estado = parametro(request, "estado");
        String numero = parametro(request, "numero");

        // devolve o que foi digitado para a tela de confirmacao, caso de algum erro
        request.setAttribute("etapa", "confirmar");
        request.setAttribute("cnpj", cnpj);
        request.setAttribute("nomeEndereco", nomeEndereco);
        request.setAttribute("rua", rua);
        request.setAttribute("bairro", bairro);
        request.setAttribute("cidade", cidade);
        request.setAttribute("estado", estado);
        request.setAttribute("numero", numero);
        request.setAttribute("complemento", complemento);
        request.setAttribute("cep", cep);

        if (nomeEndereco.isEmpty() || nomeEndereco.length() > 50) {
            mostrarErro(request, response, PAGINA_ENDERECO, "Informe um nome para o endereço (até 50 caracteres)");
            return;
        }

        if (!RegexUtils.validarCnpj(cnpjInformado)) {
            mostrarErro(request, response, PAGINA_ENDERECO, "CNPJ inválido");
            return;
        }

        if (!RegexUtils.validarCep(cepInformado)) {
            mostrarErro(request, response, PAGINA_ENDERECO, "CEP inválido");
            return;
        }

        if (rua.isEmpty() || bairro.isEmpty() || cidade.isEmpty() || estado.length() != 2 || numero.isEmpty() || cep.isEmpty()) {
            mostrarErro(request, response, PAGINA_ENDERECO, "Preencha todos os campos obrigatórios");
            return;
        }

        if (!ConsultaCnpjService.cnpjValido(cnpj)) {
            mostrarErro(request, response, PAGINA_ENDERECO, "CNPJ inválido");
            return;
        }

        Endereco endereco = new Endereco(nomeEndereco, rua, bairro, complemento, cidade, estado, numero, cep, cnpj, instituicao.getId());

        try (EnderecoDAO enderecoDAO = new EnderecoDAO()) {

            // confere de novo, pois outro cadastro pode ter usado esse cnpj depois da busca
            if (enderecoDAO.pesquisarPorCnpj(cnpj) != null) {
                mostrarErro(request, response, PAGINA_ENDERECO, "Esse CNPJ já está cadastrado");
                return;
            }

            enderecoDAO.cadastrar(endereco);

            response.sendRedirect(request.getContextPath() + "/cadastrar-endereco?action=concluido");

        } catch (SQLException e) {
            System.out.print("Erro de SQL ao cadastrar endereco da instituicao: " + e.getMessage());
            mostrarErro(request, response, PAGINA_ENDERECO, "Não foi possível salvar o endereço, tente novamente");
        }
    }

    // le o parametro sem espacos nas pontas, e vazio no lugar de null
    private String parametro(HttpServletRequest request, String nome) {

        String valor = request.getParameter(nome);

        return valor == null ? "" : valor.trim();
    }

    private void mostrarErro(HttpServletRequest request, HttpServletResponse response, String pagina, String mensagem) throws ServletException, IOException {

        request.setAttribute("erro", mensagem);

        request
                .getRequestDispatcher(pagina)
                .forward(request, response);
    }
}
