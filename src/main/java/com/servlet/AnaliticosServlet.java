package com.servlet;

import com.DAO.AnaliticosDAO;
import com.DTO.analiticos.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/analiticos")
public class AnaliticosServlet extends HttpServlet {

    private static final String AREA_RESTRITA = "/area-restrita/index.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        carregarIndicadores(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        carregarIndicadores(request, response);
    }

    private void carregarIndicadores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try(AnaliticosDAO analiticosDAO = new AnaliticosDAO()) {

            List<InstituicoesPorTipoDTO> instituicoes =
                analiticosDAO.instituicoesPorTipo();

            List<UsuariosPorAtividadeDTO> usuarios =
                    analiticosDAO.usuariosPorAtividade();

            List<ContratosPorPlanoDTO> contratos =
                    analiticosDAO.contratosPorPlano();

            List<EnderecosPorInstituicaoDTO> enderecos =
                    analiticosDAO.enderecosPorInstituicao();

            ResumoDTO resumo =
                    analiticosDAO.resumo();

            request.setAttribute("instituicoes", instituicoes);
            request.setAttribute("usuarios", usuarios);
            request.setAttribute("contratos", contratos);
            request.setAttribute("enderecos", enderecos);
            request.setAttribute("resumo", resumo);

            request.getRequestDispatcher(AREA_RESTRITA)
                    .forward(request, response);
        } catch (SQLException e) {
            System.err.println("Erro ao executar operação no banco:");
            e.printStackTrace(System.err);
            request.setAttribute("erroIndicadores", "Não foi possível carregar os indicadores. Confira a conexão com o banco e as views analíticas.");
            request.getRequestDispatcher(AREA_RESTRITA).forward(request, response);
        }
    }
}
