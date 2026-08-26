package com.DAO;

import com.model.MetodoPagamento;
import com.model.Plano;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MetodoPagamentoDAO extends DAO{
    // construtor de DAO
    public MetodoPagamentoDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    //insert
    public void cadastrar(MetodoPagamento metodoPagamento) throws SQLException{
        String descricao = metodoPagamento.getDescricao();

        String sql = """
                INSERT INTO METODO_PAGAMENTO (DESCRICAO) 
                VALUES(?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,descricao);

            pstmt.execute();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    //select
    public ArrayList<MetodoPagamento> buscar() throws SQLException{
        ArrayList<MetodoPagamento> resultado = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(
                    "SELECT * FROM PLANO ORDER BY ID");

            while(rset.next()){
                int id = rset.getInt("ID");
                String descricao = rset.getString("DESCRICAO");

                resultado.add(new MetodoPagamento(id, descricao));
            }

            return resultado;

    }

    //pesquisar por id
    public MetodoPagamento pesquisarPorId(int id) throws SQLException{
        String sql = "SELECT ID, DESCRICAO FROM METODO_PAGAMENTO WHERE id = ?";

        MetodoPagamento metodoPagamento;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String descricao = rs.getString("DESCRICAO");

                metodoPagamento = new MetodoPagamento(id, descricao);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        conn.commit();
        return metodoPagamento;
    }

    //pesquisar por descrição
    public MetodoPagamento pesquisarPorDescricao(String descricao) throws SQLException{
        String sql = "SELECT ID, DESCRICAO FROM METODO_PAGAMENTO WHERE DESCRICAO = ?";

        MetodoPagamento metodoPagamento;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, descricao);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("ID");

                metodoPagamento = new MetodoPagamento(id, descricao);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        conn.commit();
        return metodoPagamento;
    }

    //alter
    public void atualizar(MetodoPagamento original, MetodoPagamento alterado) throws SQLException{
        int id = alterado.getId();
        String descricao = alterado.getDescricao();

        StringBuilder sql = new StringBuilder("UPDATE METODO_PAGAMENTO SET");
        ArrayList<Object> alteracoes = new ArrayList<>();

        if (!original.getDescricao().equals(descricao)){
            sql.append("DESCRICAO = ?, ");
            alteracoes.add(descricao);
        }

        if (alteracoes.isEmpty()) {
            return;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        alteracoes.add(id);

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < alteracoes.size(); i++) {
                pstmt.setObject(i + 1, alteracoes.get(i));
            }

            pstmt.execute();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    //delete
    public void remover(int id) throws SQLException {

        String sql = "DELETE FROM METODO_PAGAMENTO WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            conn.commit();
        }catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}
