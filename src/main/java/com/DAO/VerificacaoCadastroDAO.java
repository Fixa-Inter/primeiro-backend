package com.DAO;

import com.model.VerificacaoCadastro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VerificacaoCadastroDAO extends DAO{

    // construtor de DAO
    public VerificacaoCadastroDAO() throws SQLException {
        super();
    }

    // insert
    public void cadastrar(VerificacaoCadastro verificacaoCadastro) throws SQLException {
        String codigoVerificacao = verificacaoCadastro.getCodigoVerificacao();
        String email = verificacaoCadastro.getEmail();

        String sql = """
                INSERT INTO VERIFICACAO_CADASTRO (CODIGO_VERIFICACAO, EMAIL)
                VALUES (?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigoVerificacao);
            pstmt.setString(2, email);

            pstmt.execute();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // select da verificacao de um email, retorna null caso n tenha nenhuma registrada
    public VerificacaoCadastro buscarCodigoValido(String email) throws SQLException {

        String sql = "SELECT ID, CODIGO_VERIFICACAO, DATA_EXPIRACAO, EMAIL, ESTA_VALIDO FROM VERIFICACAO_CADASTRO WHERE EMAIL = ? AND ESTA_VALIDO = TRUE AND DATA_EXPIRACAO > NOW() ORDER BY ID DESC LIMIT 1";

        VerificacaoCadastro verificacaoCadastro;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("ID");
                String codigoVerificacao = rs.getString("CODIGO_VERIFICACAO");
                Timestamp dataExpiracao = rs.getTimestamp("DATA_EXPIRACAO");
                Boolean estaValido = rs.getBoolean("ESTA_VALIDO");

                verificacaoCadastro = new VerificacaoCadastro(id, codigoVerificacao, dataExpiracao, email, estaValido);
            }
        }

        conn.commit();
        return verificacaoCadastro;
    }

    // update
    public void atualizar(VerificacaoCadastro original, VerificacaoCadastro alterado) throws SQLException {

        String codigoVerificacao = alterado.getCodigoVerificacao();
        Timestamp dataExpiracao = alterado.getDataExpiracao();
        Boolean estaValido = alterado.getEstaValido();

        StringBuilder sql = new StringBuilder("UPDATE VERIFICACAO_CADASTRO SET ");
        List<Object> valores = new ArrayList<>();

        if (!Objects.equals(codigoVerificacao, original.getCodigoVerificacao())) {
            sql.append("CODIGO_VERIFICACAO = ?, ");
            valores.add(codigoVerificacao);
        }

        if (!Objects.equals(dataExpiracao, original.getDataExpiracao())) {
            sql.append("DATA_EXPIRACAO = ?, ");
            valores.add(dataExpiracao);
        }

        if (!Objects.equals(estaValido, original.getEstaValido())) {
            sql.append("ESTA_VALIDO = ?, ");
            valores.add(estaValido);
        }

        if (valores.isEmpty()) {
            return;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE ID = ?");
        valores.add(original.getId());

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i + 1, valores.get(i));
            }

            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // delete
    public void remover(int id) throws SQLException {

        String sql = "DELETE FROM VERIFICACAO_CADASTRO WHERE ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}
