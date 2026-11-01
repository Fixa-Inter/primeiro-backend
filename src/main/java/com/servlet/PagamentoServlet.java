package com.servlet;

import com.DAO.PagamentoDAO;
import com.DTO.PagamentoDTO;
import com.exception.ExcecaoDeJSP;
import com.utils.Parametros;
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

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/restrita/pagamentos.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/restrita/cadastro-pagamento.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/restrita/editar-pagamento.jsp";

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
                try (com.DAO.ContratoDAO contratoDAO = new com.DAO.ContratoDAO()) {
                    request.setAttribute("contratosDisponiveis", contratoDAO.buscar(null, null, null, null));
                }
                destino = PAGINA_CADASTRO;
            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PagamentoDAO dao = new PagamentoDAO()) {
                    Pagamento pagamento = dao.pesquisarPorId(id);
                    if (pagamento == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    request.setAttribute("pagamento", pagamento);
                    request.setAttribute("pagamentoDTO", dao.listarPagamentoDTO(null, null, null, null).stream()
                            .filter(dto -> dto.getId().equals(id)).findFirst().orElse(null));
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

        if (erro) response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
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

            List<PagamentoDTO> pagamentos = dao.listarPagamentoDTO(
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

        // o formulario envia o codigo do metodo de pagamento (1 a 3)
        MetodoPagamento metodoPagamento = MetodoPagamento.converterEnum(
                Parametros.inteiro(request, "metodoPagamento")
        );

        Integer fkContrato = Parametros.inteiro(request, "fkContrato");

        BigDecimal valor = Parametros.bigDecimal(request, "valor");

        Pagamento pagamento = new Pagamento(
          metodoPagamento,
          fkContrato,
          valor
        );

        try (PagamentoDAO dao = new PagamentoDAO()) {

            dao.cadastrar(pagamento, request);

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
        int id = Parametros.inteiro(request, "id");

        try (PagamentoDAO pagamentoDAO = new PagamentoDAO()){
            pagamentoDAO.remover(id, request);

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

        int id = Parametros.inteiro(request, "id");

        // o formulario envia o codigo do metodo de pagamento (1 a 3)
        MetodoPagamento metodoPagamento = MetodoPagamento.converterEnum(
                Parametros.inteiro(request, "metodoPagamento")
        );

        Integer fkContrato = Parametros.inteiro(request, "fkContrato");

        BigDecimal valor = Parametros.bigDecimal(request, "valor");


        boolean foiRealizado = Parametros.booleano(request, "foiRealizado");



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

            dao.atualizar(original, alterado, request);

            response.sendRedirect(
                    request.getContextPath() + "/pagamentos"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
