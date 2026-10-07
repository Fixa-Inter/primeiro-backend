package com.servlet;

import com.DAO.PagamentoDAO;
import com.DAO.PlanoDAO;
import com.model.Filtro;
import com.model.Pagamento;
import com.model.Plano;
import com.model.enums.MetodoPagamento;
import com.model.enums.OperacaoFiltro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PagamentoServlet", value = "/pagamentos")
public class PagamentoServlet extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String action = request.getParameter("action");

        if (action == null){
            action = "read";
        }

        if (action.equals("read")){
            listarPagamentos(request, response);
        } else if (action.equals("create")) {

            request
                    .getRequestDispatcher("/WEB-INF/views/editar-pagamentos.jsp")
                    .forward(request, response);
        } else if (action.equals("update")) {

            int id = Integer.parseInt(request.getParameter("id"));

            try (PagamentoDAO dao = new PagamentoDAO()){

                Pagamento pagamento = dao.pesquisarId(id);

                request.setAttribute("pagamentos", pagamento);

                request
                        .getRequestDispatcher("/WEB-INF/views/editar-pagamentos.jsp")
                        .forward(request, response);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            cadastrarPagamento(request, response);
        } else if ("update".equals(action)) {
            atualizarPagamento(request, response);
        } else if ("delete".equals(action)){
            deletarPagamento(request, response);
        }

    }

    private void listarPagamentos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        try (PagamentoDAO dao = new PagamentoDAO()) {

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

            List<Pagamento> pagamentos = dao.buscar(
                    filtros,
                    campoSequencia,
                    direcaoSequencia
            );

            request.setAttribute("pagamentos", pagamentos);
            request.setAttribute("filtros", filtros);

            request
                    .getRequestDispatcher("/WEB-INF/views/pagamentos.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void cadastrarPagamento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        BigDecimal valor = new BigDecimal(request.getParameter("valor"));

        String dataPagamentoParam = request.getParameter("data_pagamento");

        LocalDateTime dataPagamento = null;

        if (dataPagamentoParam != null && !dataPagamentoParam.isBlank()){
            dataPagamento = LocalDateTime.parse(dataPagamentoParam);
        }

        Integer fkContrato = Integer.parseInt(request.getParameter("fk_contrato_id"));

        int metodoPagamento = Integer.parseInt(request.getParameter("metodo_pagamento"));

        Pagamento pagamento = new Pagamento(
                valor,
                dataPagamento,
                fkContrato,
                MetodoPagamento.converterEnum(metodoPagamento)
                );

        try (PagamentoDAO dao = new PagamentoDAO()){

            dao.cadastrar(pagamento);

            response.sendRedirect(
                    request.getContextPath() + "/pagamentos"
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void deletarPagamento(HttpServletRequest request, HttpServletResponse response){

        int id = Integer.parseInt(request.getParameter("id"));

        try (PagamentoDAO dao = new PagamentoDAO()){
            dao.remover(id);

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

    public void atualizarPagamento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        Integer id = Integer.parseInt(request.getParameter("id"));
        BigDecimal valor = new BigDecimal(request.getParameter("valor"));

        String dataPagamentoParam = request.getParameter("data_pagamento");
        LocalDateTime dataPagamento = null;

        if (dataPagamentoParam != null && !dataPagamentoParam.isBlank()) {
            // O parse direto funciona bem com o formato enviado pelo HTML5 (ex: 2023-10-25T15:30)
            dataPagamento = LocalDateTime.parse(dataPagamentoParam);
        }

        Boolean foiRealizado = Boolean.parseBoolean(request.getParameter("foiRealizado"));
        Integer fkContrato = Integer.parseInt(request.getParameter("fk_contrato_id"));
        Integer metodoPagamento = Integer.parseInt(request.getParameter("metodo_pagamento"));

        try (PagamentoDAO dao = new PagamentoDAO()){

            // Busca como está atualmente no banco
            Pagamento original = dao.pesquisarId(id);

            // Monta o objeto com os novos dados
            Pagamento alterado = new Pagamento(
                    id,
                    valor,
                    dataPagamento,
                    foiRealizado,
                    fkContrato,
                    MetodoPagamento.converterEnum(metodoPagamento)
            );

            dao.atualizar(original, alterado);

            response.sendRedirect(
                    request.getContextPath() + "/pagamentos"
            );


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}