package com.DAO;

import com.model.Endereco;
import com.model.Instituicao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Grava a instituição e o primeiro endereço na mesma transação
public class CadastroDAO extends DAO {

    // Abre a conexão com o banco
    public CadastroDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // Insere a instituição e o endereço juntos e devolve o id da instituição
    public int finalizarCadastro(Instituicao instituicao, Endereco endereco) throws SQLException {

        String sqlInstituicao = """
                INSERT INTO instituicao (nome, email_corporativo, dominio_email, tipo_instituicao, senha_hash)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;

        String sqlEndereco = """
                INSERT INTO endereco (nome, rua, bairro, complemento, cidade, estado, numero, cep, cnpj, fk_instituicao_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try {
            int idInstituicao;

            // Insere a instituição e pega o id gerado
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInstituicao)) {
                pstmt.setString(1, instituicao.getNome());
                pstmt.setString(2, instituicao.getEmailCorporativo());
                pstmt.setString(3, instituicao.getDominioEmail());
                pstmt.setInt(4, instituicao.getTipoInstituicao().getCodigo());
                pstmt.setString(5, instituicao.getSenhaHash());

                try (ResultSet rs = pstmt.executeQuery()) {
                    rs.next();
                    idInstituicao = rs.getInt("id");
                }
            }

            // Insere o endereço ligado à instituição criada
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEndereco)) {
                pstmt.setString(1, endereco.getNome());
                pstmt.setString(2, endereco.getRua());
                pstmt.setString(3, endereco.getBairro());
                pstmt.setString(4, endereco.getComplemento());
                pstmt.setString(5, endereco.getCidade());
                pstmt.setString(6, endereco.getEstado());
                pstmt.setString(7, endereco.getNumero());
                pstmt.setString(8, endereco.getCep());
                pstmt.setString(9, endereco.getCnpj());
                pstmt.setInt(10, idInstituicao);

                pstmt.executeUpdate();
            }

            // Confirma as duas inserções juntas
            conn.commit();
            return idInstituicao;

        } catch (SQLException e) {
            conn.rollback();  // Qualquer falha desfaz as duas inserções
            throw e;
        }
    }
}
