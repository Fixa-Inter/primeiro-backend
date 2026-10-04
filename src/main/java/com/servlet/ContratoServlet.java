package com.servlet;

import com.DAO.ContratoDAO;
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
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="ContratoServlet", value = "/contratos")
public class ContratoServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null){
            action = "read";
        }

        if (action.equals("read")){
            listarContratos(request, response);
        } else if (action.equals("create")) {

            request
                    .getRequestDispatcher("/WEB-INF/views/cadastro-contrato.jsp")
                    .forward(request, response);

        } else if (action.equals("update")) {

            int id = Integer.parseInt(
                    request.getParameter("id")
            );

            try (ContratoDAO dao = new ContratoDAO()) {

                Contrato contrato = dao.pesquisarPorId(id);

                request.setAttribute("contrato", contrato);

                request
                        .getRequestDispatcher("/WEB-INF/views/editar-contrato.jsp")
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
            cadastrarContrato(request, response);
        } else if ("update".equals(action)) {
            atualizarContrato(request, response);
        } else if ("delete".equals(action)){
            deletarContrato(request, response);
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

            List<Contrato> contratos = dao.buscar(
                    filtros,
                    campoSequencia,
                    direcaoSequencia
            );

            request.setAttribute("contratos", contratos);
            request.setAttribute("filtros", filtros);

            request
                    .getRequestDispatcher("/WEB-INF/views/contratos.jsp")
                    .forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void cadastrarContrato(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        Date dataVencimentoSQL = Date.valueOf(
                request.getParameter("DATA_VENCIMENTO")
        );
        LocalDate dataVencimento = (dataVencimentoSQL == null ? null : dataVencimentoSQL.toLocalDate());

        int fkEndereco = Integer.parseInt(
                request.getParameter("FK_ENDERECO_ID")
        );
        int fkPlano = Integer.parseInt(
                request.getParameter("FK_PLANO_ID")
        );
        int statusContrato = Integer.parseInt(
                request.getParameter("STATUS_CONTRATO")
        );

        Contrato contrato = new Contrato(
                dataVencimento,
                fkPlano,
                fkEndereco,
                StatusContrato.converterEnum(statusContrato)
        );
        try (ContratoDAO dao = new ContratoDAO()) {

            dao.cadastrar(contrato);

            response.sendRedirect(
                    request.getContextPath() + "/contratos"
            );

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    private void deletarContrato(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        int id = Integer.parseInt(request.getParameter("id"));

        try (ContratoDAO dao = new ContratoDAO()){
            dao.remover(id);

            response.sendRedirect(
                    request.getContextPath() + "/contratos"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void atualizarContrato(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("ID")
        );

        Date dataVencimentoSQL = Date.valueOf(
                request.getParameter("DATA_VENCIMENTO")
        );
        LocalDate dataVencimento = (dataVencimentoSQL == null ? null : dataVencimentoSQL.toLocalDate());

        int fkEndereco = Integer.parseInt(
                request.getParameter("FK_ENDERECO_ID")
        );
        int fkPlano = Integer.parseInt(
                request.getParameter("FK_PLANO_ID")
        );
        int statusContrato = Integer.parseInt(
                request.getParameter("STATUS_CONTRATO")
        );



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


            dao.atualizar(original, alterado);

            response.sendRedirect(
                    request.getContextPath() + "/contratos"
            );

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

}
