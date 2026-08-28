package com.DAO;

import com.model.Usuario;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UsuarioDAO extends DAO{

    // map dos campos que sao filtraveis
    public static final Map<String, String> camposFiltraveis = Map.of(

            "id", "ID",
            "nome", "Nome",
            "senha_hash", "Senha",
            "esta_ativo", "Esta Ativo",
            "email", "Email",
            "data_criacao", "Data Criacao",
            "cargo", "Cargo",
            "fk_instituicao_id", "Fk Instituicao ID",
            "data_ultimo_acesso", "Data Ultimo Acesso",
            "tipo_de_acesso", "Tipo De Acesso"
    );

    // convertendo String recebida do Servlet
    public Object converterValor(String campo, String valor){
        try {
            return switch (campo) {
                case "id", "fkInstituicao" -> Integer.parseInt(valor);
                case "nome", "senha_hash", "email", "cargo", "tipo_de_acesso" -> valor;
                case "esta_ativo" -> Boolean.parseBoolean(valor);
                case "data_criacao", "data_ultimo_acesso" -> LocalDate.parse(valor);
                default -> throw new IllegalArgumentException();
            };
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    // construtor
    public UsuarioDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // insert

    public void cadastrar(Usuario usuario) throws SQLException{

        String nome = usuario.getNome();
        String senhaHash = usuario.getSenhaHash();
        boolean estaAtivo = usuario.getEstaAtivo();
        String email = usuario.getEmail();
        LocalDate dataCriacao = usuario.getDataCriacao();
        String cargo = usuario.getCargo();
        String tipoDeAcesso = usuario.getTipoDeAcesso();
        int fkInstituicao = usuario.getFkInstituicao();
        LocalDate dataUltimoAcesso = usuario.getDataUltimoAcesso();

        String sql = """
                     INSERT INTO usuario (nome, senha_hash, esta_ativo, email, data_criacao, cargo, fk_instituicao_id, data_ultimo_acesso, tipo_de_acesso)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                     """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, nome);
            pstmt.setString(2, senhaHash);
            pstmt.setBoolean(3, estaAtivo);
            pstmt.setString(4, email);
            pstmt.setObject(5, dataCriacao);
            pstmt.setString(6, cargo);
            pstmt.setInt(7, fkInstituicao);
            pstmt.setObject(8, dataUltimoAcesso);
            pstmt.setString(9, tipoDeAcesso);

            pstmt.execute();

            conn.commit();
        }  catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // select
    public List<Usuario> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {

        boolean temFiltro = true;

        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT id, nome, senha_hash, esta_ativo, email, data_criacao, cargo, fk_instituicao_id, data_ultimo_acesso, tipo_de_acesso FROM usuario";

        if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)){
            sql += " WHERE %s = ?".formatted(campoFiltro);
        } else {
            temFiltro = false;
        }

        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)){
            sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);
        } else {
            sql += "ORDER BY ID ASC";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            if (temFiltro){
                pstmt.setObject(1, valorFiltro);
            }

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){

                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String senhaHash = rs.getString("senha_hash");
                    boolean estaAtivo = rs.getBoolean("esta_ativo");
                    String email = rs.getString("email");
                    Date dataCriacaoSQL = rs.getDate("data_criacao");
                    LocalDate dataCriacao = (dataCriacaoSQL == null ? null : dataCriacaoSQL.toLocalDate());
                    String cargo = rs.getString("cargo");
                    String tipoDeAcesso = rs.getString("tipo_de_acesso");
                    int fkInstituicao = rs.getInt("fk_instituicao_id");
                    Date dataUltimoAcessoSQL =  rs.getDate("data_ultimo_acesso");
                    LocalDate dataUltimoAcesso = (dataCriacaoSQL == null ? null : dataCriacaoSQL.toLocalDate());


                    usuarios.add(new Usuario(id, nome, senhaHash, estaAtivo, email, dataCriacao, cargo, tipoDeAcesso, fkInstituicao, dataUltimoAcesso));
                }
            }

        }
        conn.commit();
        return usuarios;
    }

    // select id
    public Usuario pesquisarPorId(int idUsuario) throws SQLException{


        String sql = "SELECT id, nome, senha_hash, esta_ativo, email, data_criacao, cargo, fk_instituicao_id, data_ultimo_acesso, tipo_de_acesso FROM usuario WHERE id = ?";

        Usuario u;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao encontrar aluno");
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String senhaHash = rs.getString("senha_hash");
                boolean estaAtivo = rs.getBoolean("esta_ativo");
                String email = rs.getString("email");
                Date dataCriacaoSQL = rs.getDate("data_criacao");
                LocalDate dataCriacao = (dataCriacaoSQL == null ? null : dataCriacaoSQL.toLocalDate());
                String cargo = rs.getString("cargo");
                String tipoDeAcesso = rs.getString("tipo_de_acesso");
                int fkInstituicao = rs.getInt("fk_instituicao_id");
                Date dataUltimoAcessoSQL =  rs.getDate("data_ultimo_acesso");
                LocalDate dataUltimoAcesso = (dataCriacaoSQL == null ? null : dataCriacaoSQL.toLocalDate());


                u = new Usuario(id, nome, senhaHash, estaAtivo, email, dataCriacao, cargo, tipoDeAcesso, fkInstituicao, dataUltimoAcesso);

            }

        }
        conn.commit();
        return u;
    }


    // select nome
    public Usuario pesquisarPorNome(String nomeUsuario) throws SQLException{

        String sql = "SELECT id, nome, senha_hash, esta_ativo, email, data_criacao, cargo, fk_instituicao_id, data_ultimo_acesso, tipo_de_acesso FROM usuario WHERE nome = ?";

        Usuario u;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, nomeUsuario);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao encontrar aluno");
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String senhaHash = rs.getString("senha_hash");
                boolean estaAtivo = rs.getBoolean("esta_ativo");
                String email = rs.getString("email");
                Date dataCriacaoSQL = rs.getDate("data_criacao");
                LocalDate dataCriacao = (dataCriacaoSQL == null ? null : dataCriacaoSQL.toLocalDate());
                String cargo = rs.getString("cargo");
                String tipoDeAcesso = rs.getString("tipo_de_acesso");
                int fkInstituicao = rs.getInt("fk_instituicao_id");
                Date dataUltimoAcessoSQL =  rs.getDate("data_ultimo_acesso");
                LocalDate dataUltimoAcesso = (dataCriacaoSQL == null ? null : dataCriacaoSQL.toLocalDate());

                u = new Usuario(id, nome, senhaHash, estaAtivo, email, dataCriacao, cargo, tipoDeAcesso, fkInstituicao, dataUltimoAcesso);
            }

        }
        conn.commit();
        return u;
    }

    public void atualizar(Usuario original, Usuario alterado) throws SQLException{

        int id = alterado.getId();
        String nome = alterado.getNome();
        String senhaHash = alterado.getSenhaHash();
        boolean estaAtivo = alterado.getEstaAtivo();
        String email = alterado.getEmail();
        LocalDate dataCriacao = alterado.getDataCriacao();
        String cargo = alterado.getCargo();
        String tipoDeAcesso = alterado.getTipoDeAcesso();
        int fkInstituicao = alterado.getFkInstituicao();
        LocalDate dataUltimoAcesso = alterado.getDataUltimoAcesso();

        StringBuilder sql = new StringBuilder("UPDATE usuario SET ");
        List<Object> valores = new ArrayList<>();

        if (!Objects.equals(nome, original.getNome())){
            sql.append("nome = ?, ");
            valores.add(nome);
        }

        if (!Objects.equals(senhaHash, original.getSenhaHash())){
            sql.append("senha_hash = ?, ");
            valores.add(senhaHash);
        }

        if (!Objects.equals(estaAtivo, original.getEstaAtivo())){
            sql.append("esta_ativo = ?, ");
            valores.add(estaAtivo);
        }

        if (!Objects.equals(email, original.getEmail())){
            sql.append("email = ?, ");
            valores.add(email);
        }

        if (!Objects.equals(dataCriacao, original.getDataCriacao())){
            sql.append("data_criacao = ?, ");
            valores.add(dataCriacao);
        }

        if (!Objects.equals(cargo, original.getCargo())){
            sql.append("cargo = ?, ");
            valores.add(cargo);
        }

        if (!Objects.equals(tipoDeAcesso, original.getTipoDeAcesso())){
            sql.append("tipo_de_acesso = ?, ");
            valores.add(tipoDeAcesso);
        }

        if (!Objects.equals(fkInstituicao, original.getFkInstituicao())){
            sql.append("fk_instituicao_id = ?, ");
            valores.add(fkInstituicao);
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

    public void remover(int id) throws SQLException{

        String sql = "DELETE from usuario where id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

    }
}