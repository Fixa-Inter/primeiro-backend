package com.servlet;

import com.DAO.PagamentoDAO;
import com.exception.ExcecaoDeJSP;
import com.model.Filtro;
import com.model.Pagamento;
import com.model.enums.MetodoPagamento;
import com.model.enums.OperacaoFiltro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PagamentoServlet", value = "/pagamentos")
public class PagamentoServlet extends HttpServlet {

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/pagamentos.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/cadastro-pagamento.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/editar-pagamento.jsp";
    private static final String PAGINA_ERRO = "/html/erro.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        boolean erro = true;
        String destino = null;

        if (action == null) action = "read";
        try {
            if (action.equals("read")) {
                listarPagamentos(request, response);
                destino = PAGINA_PRINCIPAL;
            } else if (action.equals("create")) {
                destino = PAGINA_CADASTRO;
            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PagamentoDAO dao = new PagamentoDAO()) {
                    Pagamento pagamento = dao.pesquisarPorId(id);
                    request.setAttribute("pagamento", pagamento);
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

        if (erro) response.sendRedirect(request.getContextPath() + PAGINA_ERRO);
        else request.getRequestDispatcher(destino).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                cadastrarPagamento(request, response);
            } else if ("update".equals(action)) {
                atualizarPagamento(request, response);
            } else if ("delete".equals(action)){
                deletarPagamento(request, response);
            }
        } catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
        }
    }

    private void listarPagamentos(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (PagamentoDAO dao = new PagamentoDAO()) {

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

            List<Pagamento> pagamentos = dao.listar(
                    filtros,
                    campoSequencia,
                    direcaoSequencia,
                    pesquisa
            );

            request.setAttribute("pagamentos", pagamentos);
            request.setAttribute("filtros", filtros);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void cadastrarPagamento(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        MetodoPagamento metodoPagamento = MetodoPagamento.converterEnum(
                request.getParameter("metodoPagamento")
        );

        Integer fkContrato = Integer.parseInt(
                request.getParameter("fkContrato")
        );

        BigDecimal valor = new BigDecimal(
                request.getParameter("valor")
        );

        //verificacoes necessarias
        if (request.getParameter("metodoPagamento").isBlank()){
            throw ExcecaoDeJSP.notNullVazio("metodoPagamento");
        }

        Pagamento pagamento = new Pagamento(
          metodoPagamento,
          fkContrato,
          valor
        );

        try (PagamentoDAO dao = new PagamentoDAO()) {

            dao.cadastrar(pagamento);

            response.sendRedirect(
                    request.getContextPath() + "/pagamentos"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void deletarPagamento(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Integer.parseInt(request.getParameter("id"));

        try (PagamentoDAO pagamentoDAO = new PagamentoDAO()){
            pagamentoDAO.remover(id);

            response.sendRedirect(
                    request.getContextPath() + "/pagamentos"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void atualizarPagamento(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("id")
        );

        MetodoPagamento metodoPagamento = MetodoPagamento.converterEnum(
                request.getParameter("metodoPagamento")
        );

        Integer fkContrato = Integer.parseInt(
                request.getParameter("fkContrato")
        );

        BigDecimal valor = new BigDecimal(
                request.getParameter("valor")
        );


        boolean foiRealizado = Boolean.parseBoolean(
                request.getParameter("foiRealizado")
        );



        try (PagamentoDAO dao = new PagamentoDAO()) {

            // Busca como está atualmente no banco
            Pagamento original = dao.pesquisarPorId(id);

            // Monta o objeto com os novos dados
            Pagamento alterado = new Pagamento(
                    id,
                    valor,
                    original.getDataPagamento(),
                    foiRealizado,
                    fkContrato,
                    metodoPagamento
            );

            dao.atualizar(original, alterado);

            response.sendRedirect(
                    request.getContextPath() + "/pagamentos"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
