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

// Fluxo público de cadastro de instituição: código, senha e endereço
@WebServlet(name = "CadastroServlet", value = "/cadastro")
public class CadastroServlet extends HttpServlet{

    // Abre a tela da etapa pedida, liberando o endereço só depois do código e da senha
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null){
            action = "escolher";
        }

        if (action.equals("escolher")){
            request
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-escolha.jsp")
                    .forward(request, response);
        } else if (action.equals("iniciar")) {
            request
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-instituicao-info.jsp")
                    .forward(request, response);
        } else if (action.equals("enderecoInfo") || action.equals("enderecoCnpj")) {
            // Só acessa o endereço quem já passou pelo código e pela senha
            if (request.getSession().getAttribute("cadastroDTO") == null
                    || request.getSession().getAttribute("emailVerificado") == null) {
                response.sendRedirect(request.getContextPath() + "/cadastro?action=iniciar");
                return;
            }

            String pagina = action.equals("enderecoInfo")
                    ? "/WEB-INF/views/publico/cadastro/cadastro-endereco-info.jsp"
                    : "/WEB-INF/views/publico/cadastro/cadastro-endereco-cnpj.jsp";

            request
                    .getRequestDispatcher(pagina)
                    .forward(request, response);
        } else if (action.equals("concluido")) {
            request
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-concluido.jsp")
                    .forward(request, response);
        }
    }

    // Executa a etapa do cadastro conforme a action e volta para a tela de origem em caso de erro
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
            // Cada erro volta para a tela de origem
            String pagina;
            if ("enviarCodigo".equals(action)) {
                pagina = "/WEB-INF/views/publico/cadastro/cadastro-instituicao-info.jsp";
            } else if ("buscarCnpj".equals(action)) {
                pagina = "/WEB-INF/views/publico/cadastro/cadastro-endereco-cnpj.jsp";
            } else {
                pagina = "/WEB-INF/views/publico/cadastro/cadastro-instituicao-endereco.jsp";
            }
            request.getRequestDispatcher(pagina).forward(request, response);
        }
    }

    // Valida os dados da instituição, gera o código e envia por e-mail
    private void enviarCodigo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String nome = request.getParameter("nome");

        String tipoInstituicao = request.getParameter("tipoInstituicao");

        String emailCorporativo = request.getParameter("emailCorporativo");

        if (!RegexUtils.validarEmail(emailCorporativo)) {
            throw ExcecaoDeJSP.valorInvalido("emailCorporativo");
        }

        String dominioEmail = emailCorporativo.substring(emailCorporativo.indexOf("@") + 1);

        try (InstituicaoDAO instituicaoDAO = new InstituicaoDAO()) {

            // Erro se o domínio do e-mail já estiver cadastrado
            if (instituicaoDAO.pesquisarPorDominioEmail(dominioEmail) != null){

                request.setAttribute("erro", ExcecaoDeJSP.dominioDuplicado().getMessage());

                request
                        .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-instituicao-info.jsp")
                        .forward(request, response);

                return;
            }

            // Guarda os dados no DTO da sessão

            CadastroInstituicaoEnderecoDTO dto = new CadastroInstituicaoEnderecoDTO();
            dto.setNome(nome);
            dto.setTipoInstituicao(tipoInstituicao);
            dto.setEmailCorporativo(emailCorporativo);
            dto.setDominioEmail(dominioEmail);

            HttpSession session = request.getSession();
            session.setAttribute("cadastroDTO", dto);
            session.removeAttribute("emailVerificado");  // Novo e-mail exige confirmar o código de novo

            // Gera o código e grava na verificacao_cadastro
            String codigo = new GeradorCodigo().gerar();

            try (VerificacaoCadastroDAO verificacaoCadastroDAO = new VerificacaoCadastroDAO()){

                VerificacaoCadastro verificacaoCadastro = new VerificacaoCadastro(codigo, emailCorporativo);

                verificacaoCadastroDAO.cadastrar(verificacaoCadastro);

            }

            // Envia o código por e-mail
            new EnviadorEmail().enviarCodigo(emailCorporativo, nome, codigo);

            // Abre a tela do código
            request
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-codigo.jsp")
                    .forward(request, response);
        } catch (SQLException e){
            System.out.print("Erro de SQL no servlet: " + e.getMessage());

            request.setAttribute("erro", "Erro ao acessar o sistema, tente novamente");

            request
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-instituicao-info.jsp")
                    .forward(request, response);
        }

    }

    // Confere o código digitado e libera as próximas etapas
    private void  verificarCodigo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        // Junta os 6 campos de dígitos (codigo1 a codigo6) em um código
        StringBuilder digitos = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            String digito = request.getParameter("codigo" + i);
            digitos.append(digito == null ? "" : digito.trim());
        }
        String codigoDigitado = digitos.toString();

        HttpSession session = request.getSession();
        CadastroInstituicaoEnderecoDTO dto = (CadastroInstituicaoEnderecoDTO) session.getAttribute("cadastroDTO");

        // Sem cadastro em andamento na sessão, volta para o início
        if (dto == null){
            response.sendRedirect(request.getContextPath() + "/cadastro?action=iniciar");
            return;
        }

        String email = dto.getEmailCorporativo();

        try (VerificacaoCadastroDAO verificacaoCadastroDAO = new VerificacaoCadastroDAO()){

            VerificacaoCadastro verificacao = verificacaoCadastroDAO.buscarCodigoValido(email);

            if (verificacao == null || !verificacao.getCodigoVerificacao().equals(codigoDigitado)){

                request
                        .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-erro-codigo.jsp")
                        .forward(request, response);
                return;
            }

            // Apaga o código para não ser reutilizado
            verificacaoCadastroDAO.invalidarPorEmail(email);

            // A partir daqui as próximas etapas são liberadas
            session.setAttribute("emailVerificado", true);

            request
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-senha.jsp")
                    .forward(request, response);
        } catch (SQLException e){
            System.out.print("Erro de sql ao verificar codigo do email: " + e.getMessage());

            request.setAttribute("erro", "Erro ao acessar o sistema, tente novamente");

            request
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-codigo.jsp")
                    .forward(request, response);
        }

    }

    // Valida a senha e guarda o hash na sessão
    private void definirSenha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String senha = request.getParameter("senha");
        String confirmarSenha = request.getParameter("confirmarSenha");

        HttpSession session = request.getSession();
        CadastroInstituicaoEnderecoDTO dto = (CadastroInstituicaoEnderecoDTO) session.getAttribute("cadastroDTO");

        // Sem cadastro na sessão ou sem código verificado, volta para o início
        if (dto == null || session.getAttribute("emailVerificado") == null){
            response.sendRedirect(request.getContextPath() + "/cadastro?action=iniciar");
            return;
        }

        // Valida se as senhas não estão vazias e são iguais
        if (senha == null || senha.isBlank() || !senha.equals(confirmarSenha)){

            request.setAttribute("erro", "As senhas não coincidem");

            request
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-senha.jsp")
                    .forward(request, response);

            return;
        }

        // Valida a regra da senha (8 caracteres, uma letra e um número)
        if (!RegexUtils.validarSenha(senha)){

            request.setAttribute("erro", ExcecaoDeJSP.MENSAGEM_SENHA_FRACA);

            request
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-senha.jsp")
                    .forward(request, response);

            return;
        }

        // Gera o hash e guarda no DTO da sessão
        String senhaHash = HashSenha.gerarHash(senha);
        dto.setSenha(senhaHash);

        session.setAttribute("cadastroDTO", dto);

        // Vai para a etapa de endereço
        request
                .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-endereco-info.jsp")
                .forward(request, response);
    }

    // Consulta o CNPJ na API e abre a confirmação dos dados
    private void buscarCnpj(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String cnpj = request.getParameter("cnpj");

        if (!RegexUtils.validarCnpj(cnpj)) {
            throw ExcecaoDeJSP.valorInvalido("cnpj");
        }

        HttpSession session = request.getSession();

        CadastroInstituicaoEnderecoDTO dto = (CadastroInstituicaoEnderecoDTO) session.getAttribute("cadastroDTO");

        if (dto == null || session.getAttribute("emailVerificado") == null) {
            response.sendRedirect(request.getContextPath() + "/cadastro?action=iniciar");
            return;
        }

        String cnpjFormatado = cnpj.replaceAll("\\D", "");

        try (EnderecoDAO enderecoDAO = new EnderecoDAO()){

            // Erro se o CNPJ já estiver cadastrado
            if (enderecoDAO.pesquisarPorCnpj(cnpjFormatado) != null){

                request.setAttribute("erro", ExcecaoDeJSP.cnpjDuplicado().getMessage());
                request.setAttribute("cnpj", cnpj);

                request
                        .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-endereco-cnpj.jsp")
                        .forward(request, response);
                return;
            }

            // Consulta a API de CNPJ

            EnderecoViaCnpjDTO dados = new ConsultaCnpjService().consultar(cnpjFormatado);

            if (dados == null) {

                request.setAttribute("erro", ExcecaoDeJSP.cnpjNaoEncontrado().getMessage());
                request.setAttribute("cnpj", cnpj);

                request
                        .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-endereco-cnpj.jsp")
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
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-instituicao-endereco.jsp")
                    .forward(request, response);

        } catch (SQLException e){
            System.out.print("Erro de sql ao consultar API de cnpj: " + e.getMessage());

            request.setAttribute("erro", "Erro ao acessar o sistema, tente novamente");
            request.setAttribute("cnpj", cnpj);

            request
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-endereco-cnpj.jsp")
                    .forward(request, response);
        }

    }

    // Grava a instituição e o endereço e conclui o cadastro
    private void finalizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        CadastroInstituicaoEnderecoDTO dto = (CadastroInstituicaoEnderecoDTO) session.getAttribute("cadastroDTO");

        if (dto == null || session.getAttribute("emailVerificado") == null) {
            response.sendRedirect(request.getContextPath() + "/cadastro?action=iniciar");
            return;
        }

        // Dados do formulário após a consulta à API
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
            request.getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-instituicao-endereco.jsp")
                    .forward(request, response);
            return;
        }

        // Monta a instituição
        Instituicao instituicao = new Instituicao(
                dto.getNome(),
                dto.getEmailCorporativo(),
                TipoInstituicao.converterEnum(Integer.parseInt(dto.getTipoInstituicao())),
                dto.getDominioEmail(),
                dto.getSenha()
        );

        // Monta o endereço

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

        // O CadastroDAO grava os dois e liga o endereço à instituição
        try (CadastroDAO cadastroDAO = new CadastroDAO()){
            cadastroDAO.finalizarCadastro(instituicao, endereco);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.print("Erro ao tentar cadastrar instituicao e endereco no banco de dados: " + e.getMessage());

            // Em caso de erro, volta para o endereço com o que foi digitado
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
                    .getRequestDispatcher("/WEB-INF/views/publico/cadastro/cadastro-instituicao-endereco.jsp")
                    .forward(request, response);
            return;
        }

        session.removeAttribute("cadastroDTO");
        session.removeAttribute("emailVerificado");

        response.sendRedirect(request.getContextPath() + "/cadastro?action=concluido");

    }
}
