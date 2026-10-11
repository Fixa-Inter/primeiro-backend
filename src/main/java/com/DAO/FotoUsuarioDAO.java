package com.DAO;

import com.model.FotoUsuario;
import com.model.SuperAdministrador;
import org.postgresql.core.SqlCommand;

import javax.print.URIException;
import javax.print.attribute.standard.JobKOctets;
import java.security.DrbgParameters;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FotoUsuarioDAO extends DAO{

    // construtor

    public FotoUsuarioDAO() throws SQLException {
        super();
    }

    // insert
    public void cadastrar(FotoUsuario fotoUsuario) throws SQLException {

        LocalDate data_registro = fotoUsuario.getDataRegistro();
        String url = fotoUsuario.getUrl();
        Integer fk_usuario_id = fotoUsuario.getFkUsuario();

        String sql = """
                     INSERT INTO foto_usuario (URL, FK_USUARIO_ID)
                     VALUES (?, ?)
                     """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, url);
            pstmt.setInt(2, fk_usuario_id);

            pstmt.execute();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // select

    public List<FotoUsuario> listar(String pesquisa) throws SQLException {

        List<FotoUsuario> fotos = new ArrayList<>();

        String sql = """
            SELECT id, data_registro, url, fk_usuario_id
            FROM foto_usuario
            ORDER BY id ASC
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                int id =
                        rs.getInt("id");

                Date dataRegistroSQL =
                        rs.getDate("data_registro");

                LocalDate dataRegistro =
                        dataRegistroSQL == null
                                ? null
                                : dataRegistroSQL.toLocalDate();

                String url =
                        rs.getString("url");

                int fkUsuario =
                        rs.getInt("fk_usuario_id");

                FotoUsuario foto =
                        new FotoUsuario(
                                id,
                                dataRegistro,
                                url,
                                fkUsuario
                        );

                fotos.add(foto);
            }
        }

        if (pesquisa != null && !pesquisa.isEmpty()) {
            String pesquisaNormalizada = pesquisa.toLowerCase().trim().replace(" ", "");
            fotos.removeIf(foto -> !foto.toString()
                    .toLowerCase().trim().replace(" ", "")
                    .contains(pesquisaNormalizada));
        }

        conn.commit();

        return fotos;
    }

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

    public FotoUsuario pesquisarPorId(int id) {
        String sql = "SELECT URL, DATA_REGISTRO, fk_usuario_id FROM foto_usuario WHERE id = ?";
        FotoUsuario fotoUsuario;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String url = rs.getString("URL");
                Date dataRegistroSQL = rs.getDate("data_registro");
                LocalDate dataRegistro = (dataRegistroSQL == null ? null : dataRegistroSQL.toLocalDate());
                Integer fkUsuario = rs.getInt("fk_usuario_id");

                fotoUsuario = new FotoUsuario(id, dataRegistro, url, fkUsuario);
            }
        }catch (SQLException e) {
            throw new RuntimeException();
        }
        return fotoUsuario;
    }

    // update
    public void atualizar(FotoUsuario original, FotoUsuario alterado) throws SQLException {

        Integer id = alterado.getId();
        String url = alterado.getUrl();
        Integer fk_usuario_id = alterado.getFkUsuario();


        StringBuilder sql = new StringBuilder("UPDATE foto_usuario SET ");
        List<Object> valores = new ArrayList<>();

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
