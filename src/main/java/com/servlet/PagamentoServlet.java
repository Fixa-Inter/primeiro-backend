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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null){
            action = "read";
        }

        if (action.equals("read")){
            listarPagamentos(request, response);
        } else if (action.equals("create")) {

            request
                    .getRequestDispatcher("/WEB-INF/views/cadastro-pagamento.jsp")
                    .forward(request, response);

        } else if (action.equals("update")) {

            int id = Integer.parseInt(
                    request.getParameter("id")
            );

            try (PagamentoDAO dao = new PagamentoDAO()) {

                Pagamento pagamento = dao.pesquisarPorId(id);

                request.setAttribute("pagamento", pagamento);

                request
                        .getRequestDispatcher("/WEB-INF/views/editar-pagamento.jsp")
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
            cadastrarPagamento(request, response);
        } else if ("update".equals(action)) {
            atualizarPagamento(request, response);
        } else if ("delete".equals(action)){
            deletarPagamento(request, response);
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

            request
                    .getRequestDispatcher("/WEB-INF/views/pagamentos.jsp")
                    .forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
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

        } catch (SQLException | ClassNotFoundException e) {
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
        } catch (ClassNotFoundException e) {
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

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
}
