package com.servlet;

import com.DAO.EnderecoDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Endereco;
import com.model.Filtro;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "EnderecoServlet", value = "/enderecos")
public class EnderecoServlet extends HttpServlet{

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/enderecos.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/cadastro-endereco.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/editar-endereco.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null){
            action = "read";
        }

        boolean erro = true;
        String destino = null;

        if (action == null) {
            action = "read";
        }
        try {
            if (action.equals("read")) {
                listarEnderecos(request, response);
                destino = PAGINA_PRINCIPAL;
            } else if (action.equals("create")) {
                destino = PAGINA_CADASTRO;
            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (EnderecoDAO dao = new EnderecoDAO()) {
                    Endereco endereco = dao.pesquisarPorId(id);
                    request.setAttribute("endereco", endereco);
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
                cadastrarEndereco(request, response);
            } else if ("update".equals(action)) {
                atualizarEndereco(request, response);
            } else if ("delete".equals(action)){
                deletarEndereco(request, response);
            }
        } catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
        }
    }

    private void listarEnderecos(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (EnderecoDAO dao = new EnderecoDAO()) {

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

            List<Endereco> enderecos = dao.listar(
                    filtros,
                    campoSequencia,
                    direcaoSequencia,
                    pesquisa
            );

            request.setAttribute("enderecos", enderecos);
            request.setAttribute("filtros", filtros);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void cadastrarEndereco(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String rua = request.getParameter("rua");

        String bairro = request.getParameter("bairro");

        String complemento = request.getParameter("complemento");

        String cidade = request.getParameter("cidade");

        String estado = request.getParameter("estado");

        String numero = request.getParameter("numero");

        String cep = request.getParameter("cep");

        String cnpj = request.getParameter("cnpj");

        Integer fkInstituicao = Integer.parseInt(
                request.getParameter("fkInstituicao")
        );

        Endereco endereco = new Endereco(
                rua,
                bairro,
                complemento,
                cidade,
                estado,
                numero,
                cep,
                cnpj,
                fkInstituicao
        );

        try (EnderecoDAO dao = new EnderecoDAO()) {

            if (dao.pesquisarPorCnpj(cnpj) != null){
                throw ExcecaoDeJSP.cnpjDuplicado();
            }

            dao.cadastrar(endereco);

            response.sendRedirect(
                    request.getContextPath() + "/enderecos"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void deletarEndereco(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Integer.parseInt(request.getParameter("id"));

        try (EnderecoDAO enderecoDAO = new EnderecoDAO()){
            enderecoDAO.remover(id);

            response.sendRedirect(
                    request.getContextPath() + "/enderecos"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void atualizarEndereco(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("id")
        );

        String rua = request.getParameter("rua");

        String bairro = request.getParameter("bairro");

        String complemento = request.getParameter("complemento");

        String cidade = request.getParameter("cidade");

        String estado = request.getParameter("estado");

        String numero = request.getParameter("numero");

        String cep = request.getParameter("cep");

        String cnpj = request.getParameter("cnpj");

        Integer fkInstituicao = Integer.parseInt(
                request.getParameter("fkInstituicao")
        );


        String dataCriacaoParam = request.getParameter("dataCriacao");

        LocalDateTime dataCriacao = null;

        if (dataCriacaoParam != null && !dataCriacaoParam.isBlank()) {
            dataCriacao = LocalDateTime.parse(dataCriacaoParam);
        }

        try (EnderecoDAO dao = new EnderecoDAO()) {

            // Busca como está atualmente no banco
            Endereco original = dao.pesquisarPorId(id);

            // Monta o objeto com os novos dados
            Endereco alterado = new Endereco(
                    id,
                    rua,
                    bairro,
                    complemento,
                    cidade,
                    estado,
                    numero,
                    cep,
                    cnpj,
                    original.getDataCriacao(),
                    fkInstituicao
            );

            Endereco enderecoApoioCnpj = dao.pesquisarPorCnpj(cnpj);

            if (enderecoApoioCnpj != null && enderecoApoioCnpj.getId() != id){
                throw ExcecaoDeJSP.cnpjDuplicado();
            }

            dao.atualizar(original, alterado);

            response.sendRedirect(
                    request.getContextPath() + "/enderecos"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

}
