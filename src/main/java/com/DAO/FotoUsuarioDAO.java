package com.DAO;

import com.DTO.FotoUsuarioDTO;
import com.model.Endereco;
import com.model.Filtro;
import com.model.FotoUsuario;
import com.model.enums.OperacaoFiltro;
import com.utils.desfazer.DesfazerDelete;
import com.utils.desfazer.DesfazerDeleteFactory;
import com.utils.desfazer.DesfazerInsert;
import com.utils.desfazer.DesfazerUpdate;
import com.utils.desfazer.GerenciadorDesfazer;
import jakarta.servlet.http.HttpServletRequest;
import com.model.SuperAdministrador;
import org.postgresql.core.SqlCommand;

import javax.print.URIException;
import javax.print.attribute.standard.JobKOctets;
import java.security.DrbgParameters;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FotoUsuarioDAO extends DAO{

    public static final Map<String, String> camposFiltraveis = Map.of(
            "ID", "ID",
            "DATA_REGISTRO", "Data de registro",
            "URL", "URL",
            "USUARIO_NOME", "Nome do usuário",
            "USUARIO_EMAIL", "Email do usuário"
    );

    public static final Map<String, List<OperacaoFiltro>> operacoesPorCampo = Map.of(
            "ID", List.of(OperacaoFiltro.IGUAL),
            "DATA_REGISTRO", List.of(OperacaoFiltro.IGUAL, OperacaoFiltro.MAIOR_QUE,
                    OperacaoFiltro.MAIOR_OU_IGUAL, OperacaoFiltro.MENOR_QUE, OperacaoFiltro.MENOR_OU_IGUAL),
            "URL", List.of(OperacaoFiltro.IGUAL, OperacaoFiltro.CONTEM),
            "USUARIO_NOME", List.of(OperacaoFiltro.IGUAL, OperacaoFiltro.CONTEM),
            "USUARIO_EMAIL", List.of(OperacaoFiltro.IGUAL, OperacaoFiltro.CONTEM)
    );

    // construtor

    public FotoUsuarioDAO() throws SQLException {
        super();
    }

    // insert
    public int cadastrar(FotoUsuario fotoUsuario) throws SQLException {

        String url = fotoUsuario.getUrl();
        Integer fk_usuario_id = fotoUsuario.getFkUsuario();

        String sql = """
                     INSERT INTO foto_usuario (URL, FK_USUARIO_ID)
                     VALUES (?, ?) RETURNING ID
                     """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, url);
            pstmt.setInt(2, fk_usuario_id);

            int id;
            try (ResultSet rs = pstmt.executeQuery()) { rs.next(); id = rs.getInt("ID"); }

            conn.commit();
            return id;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public int cadastrar(FotoUsuario fotoUsuario, HttpServletRequest request) throws SQLException {
        int id = cadastrar(fotoUsuario);
        GerenciadorDesfazer.registrarComando(request, new DesfazerInsert(getClass(), id));
        return id;
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

                Timestamp dataRegistroSQL = rs.getTimestamp("data_registro");
                LocalDateTime dataRegistro = dataRegistroSQL == null
                        ? null
                        : dataRegistroSQL.toLocalDateTime();

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

    public List<FotoUsuario> listar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia,
                                    String pesquisa) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id, data_registro, url, fk_usuario_id FROM foto_usuario");
        List<Object> valores = new ArrayList<>();

        if (filtros != null && !filtros.isEmpty()) {
            for (Filtro filtro : filtros) {
                String coluna = switch (filtro.getCampoFiltravel()) {
                    case "ID" -> "id";
                    case "FK_USUARIO_ID" -> "fk_usuario_id";
                    default -> throw new IllegalArgumentException("Campo não permitido para busca de foto: "
                            + filtro.getCampoFiltravel());
                };
                sql.append(valores.isEmpty() ? " WHERE " : " AND ").append(coluna).append(" = ?");
                valores.add(filtro.getValor());
            }
        }
        sql.append(" ORDER BY id ASC");

        List<FotoUsuario> fotos = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i + 1, valores.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp dataRegistroSQL = rs.getTimestamp("data_registro");
                    LocalDateTime dataRegistro = dataRegistroSQL == null ? null : dataRegistroSQL.toLocalDateTime();
                    fotos.add(new FotoUsuario(rs.getInt("id"), dataRegistro, rs.getString("url"),
                            rs.getInt("fk_usuario_id")));
                }
            }
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
                Timestamp dataRegistroSQL = rs.getTimestamp("data_registro");
                LocalDateTime data_registro = dataRegistroSQL == null ? null : dataRegistroSQL.toLocalDateTime();
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
                Timestamp dataRegistroSQL = rs.getTimestamp("data_registro");
                LocalDateTime dataRegistro = (dataRegistroSQL == null ? null : dataRegistroSQL.toLocalDateTime());
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

    public void atualizar(FotoUsuario original, FotoUsuario alterado, HttpServletRequest request) throws SQLException {
        atualizar(original, alterado);
        GerenciadorDesfazer.registrarComando(request, new DesfazerUpdate(getClass(), original));
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

    public void remover(int id, HttpServletRequest request) throws SQLException {
        DesfazerDelete comando = DesfazerDeleteFactory.criar(getClass(), id);
        remover(id);
        GerenciadorDesfazer.registrarComando(request, comando);
    }

    public List<FotoUsuarioDTO> listarFotoUsuarioDTO(String pesquisa) throws SQLException {
        List<FotoUsuarioDTO> resultado = new ArrayList<>();
        String sql = "SELECT * FROM vw_foto_usuario";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp dataSql = rs.getTimestamp("data_registro");
                    resultado.add(new FotoUsuarioDTO(rs.getInt("id"),
                            dataSql == null ? null : dataSql.toLocalDateTime(), rs.getString("url"),
                            rs.getString("usuario_nome"), rs.getString("usuario_email")));
                }
            }
        }
        if (pesquisa != null && !pesquisa.isBlank()) {
            String termo = pesquisa.toLowerCase().trim().replace(" ", "");
            resultado.removeIf(dto -> !dto.toString().toLowerCase().trim().replace(" ", "").contains(termo));
        }
        conn.commit();
        return resultado;
    }

    //cadastrar completo, ignorando os default. usado para desfazer um delete
    public int cadastrarCompleto(FotoUsuario fotoUsuario) throws SQLException {

        int id = fotoUsuario.getId();
        LocalDateTime dataRegistro = fotoUsuario.getDataRegistro();
        String url = fotoUsuario.getUrl();
        Integer fkUsuarioId = fotoUsuario.getFkUsuario();


        String sql = """
                     INSERT INTO foto_usuario (URL, FK_USUARIO_ID, ID, DATA_REGISTRO)
                     VALUES (?, ?, ?, ?)
                     """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, url);
            pstmt.setInt(2, fkUsuarioId);
            pstmt.setInt(3, id);
            pstmt.setTimestamp(4, dataRegistro == null ? null : Timestamp.valueOf(dataRegistro));

            pstmt.execute();

            //Ajusta o serial do banco caso o id do cadastro for maior que o serial atual
            String sqlAjustarSerial = "SELECT setval(pg_get_serial_sequence('foto_usuario', 'id'), COALESCE(max(id), 1)) FROM foto_usuario";
            try (PreparedStatement stmtSerial = conn.prepareStatement(sqlAjustarSerial);
                 ResultSet rs = stmtSerial.executeQuery()) {
                rs.next();
            }

            conn.commit();
            return fotoUsuario.getId();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}
