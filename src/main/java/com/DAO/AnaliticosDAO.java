package com.DAO;

import com.DTO.FotoUsuarioDTO;
import com.DTO.analiticos.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Consulta as views analíticas do painel inicial
public class AnaliticosDAO extends DAO{

    // Abre a conexão com o banco
    public AnaliticosDAO() throws SQLException {
        super();
    }

    // Conta as instituições por tipo
    public List<InstituicoesPorTipoDTO> instituicoesPorTipo() throws SQLException {

        List<InstituicoesPorTipoDTO> resultado = new ArrayList<>();

        String sql = "select * from vw_instituicao_por_tipo";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int tipo = rs.getInt("tipo_instituicao");
                    int instituicoes = rs.getInt("total");
                    int percentual = rs.getInt("percentual");

                    resultado.add(new InstituicoesPorTipoDTO(tipo, instituicoes, percentual));

                }
            }
        }

        conn.commit();
        return resultado;
    }

    // Conta os usuários ativos e inativos
    public List<UsuariosPorAtividadeDTO> usuariosPorAtividade() throws SQLException {

        List<UsuariosPorAtividadeDTO> resultado = new ArrayList<>();

        String sql = "select * from vw_usuario_por_atividade;";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    boolean ativo = rs.getBoolean("esta_ativo");
                    int usuarios = rs.getInt("total");
                    int percentual = rs.getInt("percentual");

                    resultado.add(new UsuariosPorAtividadeDTO(ativo, usuarios, percentual));

                }
            }
        }

        conn.commit();
        return resultado;
    }

    // Conta os contratos por plano
    public List<ContratosPorPlanoDTO> contratosPorPlano() throws SQLException {

        List<ContratosPorPlanoDTO> resultado = new ArrayList<>();

        String sql = "select * from vw_contrato_por_plano;";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String plano = rs.getString("plano_nome");
                    int contratos = rs.getInt("total_contratos");
                    int percentual = rs.getInt("percentual");

                    resultado.add(new ContratosPorPlanoDTO(plano, contratos, percentual));

                }
            }
        }

        conn.commit();
        return resultado;
    }

    // Conta os endereços por instituição
    public List<EnderecosPorInstituicaoDTO> enderecosPorInstituicao() throws SQLException {

        List<EnderecosPorInstituicaoDTO> resultado = new ArrayList<>();

        String sql = "select * from vw_endereco_por_instituicao;";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String instituicao = rs.getString("instituicao_nome");
                    int enderecos = rs.getInt("total_enderecos");
                    int percentual = rs.getInt("percentual");

                    resultado.add(new EnderecosPorInstituicaoDTO(instituicao, enderecos, percentual));

                }
            }
        }

        conn.commit();
        return resultado;
    }

    // Soma os totais de instituições, usuários e endereços
    public ResumoDTO resumo() throws SQLException {
        ResumoDTO resultado;

        String sql = "select * from vw_resumo;";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int instituicoes = rs.getInt("total_instituicoes");
                    int usuarios = rs.getInt("total_usuarios");
                    int enderecos = rs.getInt("total_enderecos");

                    resultado = new ResumoDTO(instituicoes, usuarios, enderecos);
                } else {
                    resultado = new ResumoDTO(0, 0, 0);
                }
            }
        }

        conn.commit();
        return resultado;
    }

}
