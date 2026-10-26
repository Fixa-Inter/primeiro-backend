package com.servlet;

import com.DAO.CadastroDAO;
import com.DAO.EnderecoDAO;
import com.DAO.InstituicaoDAO;
import com.DAO.VerificacaoCadastroDAO;
import com.DTO.CadastroInstituicaoEnderecoDTO;
import com.DTO.EnderecoViaCnpjDTO;
import com.model.Endereco;
import com.model.Instituicao;
import com.model.VerificacaoCadastro;
import com.model.enums.TipoInstituicao;
import com.service.ConsultaCnpjService;
import com.service.EnviadorEmail;
import com.service.GeradorCodigo;
import com.service.HashSenha;
import com.exception.ExcecaoDeJSP;
import com.utils.RegexUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

@WebServlet(name = "CadastroServlet", value = "/cadastro")
public class CadastroServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null){
            action = "escolher";
        }

        if (action.equals("escolher")){
            request
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-escolha.jsp")
                    .forward(request, response);
        } else if (action.equals("iniciar")) {
            request
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-instituicao-info.jsp")
                    .forward(request, response);
        } else if (action.equals("concluido")) {
            request
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-concluido.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String action = request.getParameter("action");
        try {
            if ("enviarCodigo".equals(action)){
                enviarCodigo(request, response);
            } else if ("verificarCodigo".equals(action)) {
                verificarCodigo(request, response);
            } else if ("definirSenha".equals(action)) {
                definirSenha(request, response);
            } else if ("buscarCnpj".equals(action)){
                buscarCnpj(request, response);
            } else if ("finalizar".equals(action)) {
                finalizar(request, response);
            }
        } catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            String pagina = "enviarCodigo".equals(action)
                    ? "/WEB-INF/cadastro/views/cadastro-instituicao-info.jsp"
                    : "/WEB-INF/cadastro/views/cadastro-instituicao-endereco.jsp";
            request.getRequestDispatcher(pagina).forward(request, response);
        }
    }

    private void enviarCodigo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String nome = request.getParameter("nome");

        String tipoInstituicao = request.getParameter("tipoInstituicao");

        String emailCorporativo = request.getParameter("emailCorporativo");

        if (!RegexUtils.validarEmail(emailCorporativo)) {
            throw ExcecaoDeJSP.valorInvalido("emailCorporativo");
        }

        String dominioEmail = emailCorporativo.substring(emailCorporativo.indexOf("@") + 1);

        try (InstituicaoDAO instituicaoDAO = new InstituicaoDAO()) {

            // valida se esse email nao foi cadastrado antes
            if (instituicaoDAO.pesquisarPorDominioEmail(dominioEmail) != null){

                request.setAttribute("erro", "Já existe uma instituicao com esse endereço de email");

                request
                        .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-instituicao-info.jsp")
                        .forward(request, response);

                return;
            }

            // guarda os dados no DTO da sessao

            CadastroInstituicaoEnderecoDTO dto = new CadastroInstituicaoEnderecoDTO();
            dto.setNome(nome);
            dto.setTipoInstituicao(tipoInstituicao);
            dto.setEmailCorporativo(emailCorporativo);
            dto.setDominioEmail(dominioEmail);

            HttpSession session = request.getSession();
            session.setAttribute("cadastroDTO", dto);

            // gera codigo e grava na verificacao_cadastro
            String codigo = new GeradorCodigo().gerar();

            try (VerificacaoCadastroDAO verificacaoCadastroDAO = new VerificacaoCadastroDAO()){

                VerificacaoCadastro verificacaoCadastro = new VerificacaoCadastro(codigo, emailCorporativo);

                verificacaoCadastroDAO.cadastrar(verificacaoCadastro);

            }

            // enviar codigo por email
            new EnviadorEmail().enviarCodigo(emailCorporativo, nome, codigo);

            // tela de digitar codigo
            request
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-codigo.jsp")
                    .forward(request, response);
        } catch (SQLException e){
            System.out.print("Erro de SQL no servlet: " + e.getMessage());

            request.setAttribute("erro", "Erro ao acessar o sistema, tente novamente");

            request
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-instituicao-info.jsp")
                    .forward(request, response);
        }

    }

    private void  verificarCodigo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String codigoDigitado = request.getParameter("codigo");

        HttpSession session = request.getSession();
        CadastroInstituicaoEnderecoDTO dto = (CadastroInstituicaoEnderecoDTO) session.getAttribute("cadastroDTO");

        // se não tem cadastro em andamento de acordo com a sessao, volta para o inicio
        if (dto == null){
            response.sendRedirect(request.getContextPath() + "/cadastro?action=iniciar");
            return;
        }

        String email = dto.getEmailCorporativo();

        try (VerificacaoCadastroDAO verificacaoCadastroDAO = new VerificacaoCadastroDAO()){

            VerificacaoCadastro verificacao = verificacaoCadastroDAO.buscarCodigoValido(email);

            if (verificacao == null || !verificacao.getCodigoVerificacao().equals(codigoDigitado)){

                request
                        .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-erro-codigo.jsp")
                        .forward(request, response);
                return;
            }

            // apaga o codigo para ele nao poder ser reutilizado
            verificacaoCadastroDAO.invalidarPorEmail(email);

            request
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-senha.jsp")
                    .forward(request, response);
        } catch (SQLException e){
            System.out.print("Erro de sql ao verificar codigo do email: " + e.getMessage());

            request.setAttribute("erro", "Erro ao acessar o sistema, tente novamente");

            request
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-codigo.jsp")
                    .forward(request, response);
        }

    }

    private void definirSenha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String senha = request.getParameter("senha");
        String confirmarSenha = request.getParameter("confirmarSenha");

        HttpSession session = request.getSession();
        CadastroInstituicaoEnderecoDTO dto = (CadastroInstituicaoEnderecoDTO) session.getAttribute("cadastroDTO");

        // se de acordo com a sessao nao tiver cadastro, volta para o inicio
        if (dto == null){
            response.sendRedirect(request.getContextPath() + "/cadastro?action=iniciar");
            return;
        }

        // verifica se as senhas nao estao vazias e sao iguais
        if (senha == null || senha.isBlank() || !senha.equals(confirmarSenha)){

            request.setAttribute("erro", "As senhas não coincidem");

            request
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-senha.jsp")
                    .forward(request, response);

            return;
        }

        // faz o hash e guarda na DTO da sessao
        String senhaHash = HashSenha.gerarHash(senha);
        dto.setSenha(senhaHash);

        session.setAttribute("cadastroDTO", dto);

        // vai para a tela de endereco
        request
                .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-instituicao-endereco.jsp")
                .forward(request, response);
    }

    private void buscarCnpj(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String cnpj = request.getParameter("cnpj");

        if (!RegexUtils.validarCnpj(cnpj)) {
            throw ExcecaoDeJSP.valorInvalido("cnpj");
        }

        HttpSession session = request.getSession();

        CadastroInstituicaoEnderecoDTO dto = (CadastroInstituicaoEnderecoDTO) session.getAttribute("cadastroDTO");

        if (dto == null) {
            response.sendRedirect(request.getContextPath() + "/cadastro?action=iniciar");
            return;
        }

        String cnpjFormatado = cnpj.replaceAll("\\D", "");

        try (EnderecoDAO enderecoDAO = new EnderecoDAO()){

            // verifica se esse cnpj ja esta cadastrado, e se estiver da erro
            if (enderecoDAO.pesquisarPorCnpj(cnpjFormatado) != null){

                request.setAttribute("erro", "Esse cnpj já está cadastrado");
                request.setAttribute("cnpj", cnpj);

                request
                        .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-instituicao-endereco.jsp")
                        .forward(request, response);
                return;
            }

            // consulta a api

            EnderecoViaCnpjDTO dados = new ConsultaCnpjService().consultar(cnpjFormatado);

            if (dados == null) {

                request.setAttribute("erro", "CNPJ inválido ou não encontrado");
                request.setAttribute("cnpj", cnpj);

                request
                        .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-instituicao-endereco.jsp")
                        .forward(request, response);
                return;
            }

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
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-instituicao-endereco.jsp")
                    .forward(request, response);


        } catch (SQLException e){
            System.out.print("Erro de sql ao consultar API de cnpj: " + e.getMessage());

            request.setAttribute("erro", "Erro ao acessar o sistema, tente novamente");
            request.setAttribute("cnpj", cnpj);

            request
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-instituicao-endereco.jsp")
                    .forward(request, response);
        }

    }

    private void finalizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        CadastroInstituicaoEnderecoDTO dto = (CadastroInstituicaoEnderecoDTO) session.getAttribute("cadastroDTO");

        if (dto == null) {
            response.sendRedirect(request.getContextPath() + "/cadastro?action=iniciar");
            return;
        }

        // dados do form apos chamar a API
        String cnpjInformado = request.getParameter("cnpj");
        String cepInformado = request.getParameter("cep");
        if (!RegexUtils.validarCnpj(cnpjInformado)) {
            throw ExcecaoDeJSP.valorInvalido("cnpj");
        }
        if (!RegexUtils.validarCep(cepInformado)) {
            throw ExcecaoDeJSP.valorInvalido("cep");
        }
        String cnpj = cnpjInformado.replaceAll("\\D", "");
        String cep = cepInformado.replaceAll("\\D", "");
        String nomeEndereco = request.getParameter("nomeEndereco");
        String rua = request.getParameter("rua");
        String bairro = request.getParameter("bairro");
        String complemento = request.getParameter("complemento");
        String cidade = request.getParameter("cidade");
        String estado = request.getParameter("estado");
        String numero = request.getParameter("numero");

        if (nomeEndereco == null || nomeEndereco.isBlank() || nomeEndereco.length() > 50) {
            request.setAttribute("erro", "Informe um nome para o endereço (até 50 caracteres).");
            request.setAttribute("nomeEndereco", nomeEndereco);
            request.setAttribute("cnpj", cnpjInformado);
            request.setAttribute("rua", rua);
            request.setAttribute("bairro", bairro);
            request.setAttribute("complemento", complemento);
            request.setAttribute("cidade", cidade);
            request.setAttribute("estado", estado);
            request.setAttribute("numero", numero);
            request.setAttribute("cep", cepInformado);
            request.getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-instituicao-endereco.jsp")
                    .forward(request, response);
            return;
        }

        // monta instituicao
        Instituicao instituicao = new Instituicao(
                dto.getNome(),
                dto.getEmailCorporativo(),
                TipoInstituicao.converterEnum(dto.getTipoInstituicao()),
                dto.getDominioEmail(),
                dto.getSenha()
        );

        // monta endereco

        Endereco endereco = new Endereco(
            null,
            nomeEndereco,
                rua,
                bairro,
                complemento,
                cidade,
                estado,
                numero,
                cep,
                cnpj,
                null,
                null
        );

        // cadastroDAO cadastra os dois ao mesmo tempo, e dentro dele define que a fk de endereco e o id instituicao
        try (CadastroDAO cadastroDAO = new CadastroDAO()){
            cadastroDAO.finalizarCadastro(instituicao, endereco);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.print("Erro ao tentar cadastrar instituicao e endereco no banco de dados: " + e.getMessage());

            // nao vai para a tela de concluido, volta para o endereco com o que foi digitado
            request.setAttribute("erro", "Não foi possível concluir o cadastro, tente novamente");
            request.setAttribute("cnpj", cnpj);
            request.setAttribute("rua", rua);
            request.setAttribute("bairro", bairro);
            request.setAttribute("cidade", cidade);
            request.setAttribute("estado", estado);
            request.setAttribute("numero", numero);
            request.setAttribute("complemento", complemento);
            request.setAttribute("cep", cep);

            request
                    .getRequestDispatcher("/WEB-INF/cadastro/views/cadastro-instituicao-endereco.jsp")
                    .forward(request, response);
            return;
        }

        session.removeAttribute("cadastroDTO");

        response.sendRedirect(request.getContextPath() + "/cadastro?action=concluido");

    }
}
