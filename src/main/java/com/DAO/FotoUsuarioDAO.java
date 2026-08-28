package com.DAO;

import com.model.FotoUsuario;
import org.postgresql.core.SqlCommand;

import javax.print.URIException;
import javax.print.attribute.standard.JobKOctets;
import java.security.DrbgParameters;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FotoUsuarioDAO extends DAO{

    // construtor

    public FotoUsuarioDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // insert
    public void cadastrar(FotoUsuario fotoUsuario) throws SQLException {

        LocalDate data_registro = fotoUsuario.getDataRegistro();
        String url = fotoUsuario.getUrl();
        Integer fk_usuario_id = fotoUsuario.getFkUsuario();

        String sql = """
                     INSERT INTO foto_usuario (DATA_REGISTRO, URL, FK_USUARIO_ID)
                     VALUES (?, ?, ?)
                     """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, data_registro);
            pstmt.setString(2, url);
            pstmt.setInt(3, fk_usuario_id);

            pstmt.execute();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // select pelo fk_usuario_id
    public FotoUsuario pesquisarFkID(int fkId) throws SQLException {

        String sql = "SELECT id, data_registro, url, fk_usuario_id FROM foto_usuario WHERE fk_usuario_id = ?";

        FotoUsuario fu;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, fkId);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao procurar por Foto Usuario");
                }

                int id = rs.getInt("id");
                LocalDate data_registro = rs.getDate("data_registro").toLocalDate();
                String url = rs.getString("url");
                Integer fk_usuario_id = rs.getInt("fk_usuario_id");


                fu = new FotoUsuario(id, data_registro, url, fk_usuario_id);
            }

        }
        conn.commit();
        return fu;
    }

    // update
    public void atualizar(FotoUsuario original, FotoUsuario alterado) throws SQLException {

        Integer id = alterado.getId();
        LocalDate data_registro = alterado.getDataRegistro();
        String url = alterado.getUrl();
        Integer fk_usuario_id = alterado.getFkUsuario();


        StringBuilder sql = new StringBuilder("UPDATE foto_usuario SET ");
        List<Object> valores = new ArrayList<>();


        if (!Objects.equals(data_registro, original.getDataRegistro())){
            sql.append("data_registro = ?, ");
            valores.add(data_registro);
        }

        if (!Objects.equals(url, original.getUrl())){
            sql.append("url = ?, ");
            valores.add(url);
        }

        if (!Objects.equals(fk_usuario_id, original.getFkUsuario())){
            sql.append("fk_usuario_id = ?, ");
            valores.add(fk_usuario_id);
        }

        if (valores.isEmpty()){
            return;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        valores.add(id);

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())){
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i + 1, valores.get(i));
            }

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e){
            conn.rollback();
            throw e;
        }
    }


    // delete
    public void remover(int id) throws SQLException {


        String sql = "DELETE FROM foto_usuario WHERE ID = ?";

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)){
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e){
            conn.rollback();
            throw e;
        }
    }

}