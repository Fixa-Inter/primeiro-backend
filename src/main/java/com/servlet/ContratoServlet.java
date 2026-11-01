package com.servlet;

import com.DAO.ContratoDAO;
import com.DTO.ContratoDTO;
import com.exception.ExcecaoDeJSP;
import com.utils.Parametros;
import com.model.Contrato;
import com.model.Filtro;
import com.model.enums.OperacaoFiltro;
import com.model.enums.StatusContrato;
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



@WebServlet(name="ContratoServlet", value = "/contratos")
public class ContratoServlet extends HttpServlet{

    private static final String PAGINA_PRINCIPAL = "/WEB-INF/views/restrita/contratos.jsp";
    private static final String PAGINA_CADASTRO = "/WEB-INF/views/restrita/cadastro-contrato.jsp";
    private static final String PAGINA_EDICAO = "/WEB-INF/views/restrita/editar-contrato.jsp";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        boolean erro = true;
        String destino = null;

        if (action == null){
            action = "read";
        }
        try {
            if (action.equals("read")) {
                listarContratos(request, response);
                destino = PAGINA_PRINCIPAL;
            }
            else if (action.equals("create")) {
                try (com.DAO.EnderecoDAO enderecoDAO = new com.DAO.EnderecoDAO();
                     com.DAO.PlanoDAO planoDAO = new com.DAO.PlanoDAO()) {
                    request.setAttribute("enderecosDisponiveis", enderecoDAO.listar(null, null, null, null));
                    request.setAttribute("planosDisponiveis", planoDAO.buscar(null, null, null, null));
                }
                destino = PAGINA_CADASTRO;
            }
            else if (action.equals("update")) {

                int id = Integer.parseInt(
                        request.getParameter("id")
                );

                try (ContratoDAO dao = new ContratoDAO()) {
                    Contrato contrato = dao.pesquisarPorId(id);
                    if (contrato == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    request.setAttribute("contrato", contrato);
                    request.setAttribute("contratoDTO", dao.listarContratoDTO(null, null, null, null).stream()
                            .filter(dto -> dto.getId().equals(id)).findFirst().orElse(null));
                }
                // listas para os dropdowns de endereco e plano da tela de edicao
                try (com.DAO.EnderecoDAO enderecoDAO = new com.DAO.EnderecoDAO();
                     com.DAO.PlanoDAO planoDAO = new com.DAO.PlanoDAO()) {
                    request.setAttribute("enderecosDisponiveis", enderecoDAO.listar(null, null, null, null));
                    request.setAttribute("planosDisponiveis", planoDAO.buscar(null, null, null, null));
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

        try{
            if ("create".equals(action)) {
                cadastrarContrato(request, response);
            } else if ("update".equals(action)) {
                atualizarContrato(request, response);
            } else if ("delete".equals(action)){
                deletarContrato(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }catch (ExcecaoDeJSP e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
        }
    }

    private void listarContratos(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try (ContratoDAO dao = new ContratoDAO()) {

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

            List<ContratoDTO> contratos = dao.listarContratoDTO(
                    filtros,
                    campoSequencia,
                    direcaoSequencia,
                    pesquisa
            );

            request.setAttribute("contratos", contratos);
            request.setAttribute("filtros", filtros);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void cadastrarContrato(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        LocalDate dataVencimento = Parametros.data(request, "dataVencimento");

        int fkEndereco = Parametros.inteiro(request, "fkEndereco");
        int fkPlano = Parametros.inteiro(request, "fkPlano");

        int statusContrato = Parametros.inteiro(request, "statusContrato");

        Contrato contrato = new Contrato(
                dataVencimento,
                fkPlano,
                fkEndereco,
                StatusContrato.converterEnum(statusContrato)
        );
        try (ContratoDAO dao = new ContratoDAO()) {

            dao.cadastrar(contrato, request);

            response.sendRedirect(
                    request.getContextPath() + "/contratos"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void deletarContrato(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Parametros.inteiro(request, "id");

        try (ContratoDAO dao = new ContratoDAO()){
            dao.remover(id, request);

            response.sendRedirect(
                    request.getContextPath() + "/contratos"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void atualizarContrato(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Parametros.inteiro(request, "id");

        LocalDate dataVencimento = Parametros.data(request, "dataVencimento");

        int fkEndereco = Parametros.inteiro(request, "fkEndereco");
        int fkPlano = Parametros.inteiro(request, "fkPlano");
        int statusContrato = Parametros.inteiro(request, "statusContrato");



        try (ContratoDAO dao = new ContratoDAO()) {

            // Busca como está atualmente no banco
            Contrato original = dao.pesquisarPorId(id);

            // Monta o objeto com os novos dados
            Contrato alterado = new Contrato(
                    id,
                    original.getDataInicio(),
                    dataVencimento,
                    fkEndereco,
                    fkPlano,
                    StatusContrato.converterEnum(statusContrato)
            );


            dao.atualizar(original, alterado, request);

            response.sendRedirect(
                    request.getContextPath() + "/contratos"
            );

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

}
