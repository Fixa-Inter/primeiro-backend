package com.DAO;

import com.model.SuperAdministrador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;

public class SuperAdministradorDAO extends DAO{
    public static final Map<String,String> camposFiltraveis = Map.of(
            "nome", "Nome",
            "funcao", "Funcao",
            "email", "Email"
    );

    // Metodo que converte o valor de acordo com o campo que será filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "id" -> Integer.parseInt(valor);
                case "nome", "funcao", "email" -> valor;
                default -> throw new IllegalArgumentException();
            };
        }catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    // construtor de DAO
    public SuperAdministradorDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    //insert
    public void cadastrar(SuperAdministrador superAdministrador) throws SQLException{
        Integer id = superAdministrador.getId();
        String funcao = superAdministrador.getFuncao();
        String nome = superAdministrador.getNome();
        String senhaHash = superAdministrador.getSenhaHash();
        String email = superAdministrador.getEmail();

        String sql = """
                INSERT INTO SUPER_ADMINISTRADOR (FUNCAO,NOME,SENHA_HASH,EMAIL) 
                VALUES(?,?,?,?)
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,funcao);
            pstmt.setString(2,nome);
            pstmt.setString(3,senhaHash);
            pstmt.setString(4,email);

            pstmt.execute();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    //select
    public ArrayList<SuperAdministrador> buscar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException{
        boolean temFiltro = true;

        ArrayList<SuperAdministrador> resultado = new ArrayList<>();
        String sql = "SELECT ID, NOME, FUNCAO, EMAIL FROM SUPER_ADMINISTRADOR";

        // Verificando campo de filtragem
        if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)) {
            sql += " WHERE %s = ?".formatted(campoFiltro);
        } else {
            temFiltro = false;
        }

        // Verificando campo e direcao da ordenação
        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)) {
            sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);
        } else {
            sql += " ORDER BY id ASC";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Verifica se tem filtro, se sim define a variável do comando SQL
            if (temFiltro) {
                pstmt.setObject(1, valorFiltro);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String nome = rs.getString("NOME");
                    String funcao = rs.getString("FUNCAO");
                    String email = rs.getString("EMAIL");

                    resultado.add(new SuperAdministrador(id, nome, funcao, null,email));
                }
            }
        }

        conn.commit();
        return resultado;
    }

    //pesquisar por id
    public SuperAdministrador pesquisarPorId(int id) throws SQLException {
        String sql = "SELECT NOME, FUNCA0, EMAIL FROM SUPER_ADMINISTRADOR WHERE id = ?";
        SuperAdministrador superAdministrador;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String nome = rs.getString("NOME");
                String funcao = rs.getString("FUNCAO");
                String email = rs.getString("EMAIL");
                String senhaHash = rs.getString("SENHA_HASH");

                superAdministrador = new SuperAdministrador(id, funcao, nome, senhaHash, email);
            }
        }catch (SQLException e) {
            throw new RuntimeException();
        }
        return superAdministrador;
    }

    //pesquisar por email
    public SuperAdministrador pesquisarPorEmail(String email){
        {
            String sql = "SELECT NOME, FUNCA0, EMAIL FROM SUPER_ADMINISTRADOR WHERE EMAIL = ?";
            SuperAdministrador superAdministrador;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    int id = rs.getInt("ID");
                    String funcao = rs.getString("FUNCAO");
                    String nome = rs.getString("NOME");
                    String senhaHash = rs.getString("SENHA_HASH");

                    superAdministrador = new SuperAdministrador(id, funcao, nome, senhaHash, email);
                }
            }catch (SQLException e) {
                throw new RuntimeException();
            }
            return superAdministrador;
        }
    }

    //alter
    public void atualizar(SuperAdministrador original, SuperAdministrador alterado) throws SQLException{
        String nome = alterado.getNome();
        String email = alterado.getEmail();
        String funcao = alterado.getFuncao();
        String senhaHash = alterado.getSenhaHash();

        StringBuilder sql = new StringBuilder("UPDATE SUPER_ADMINISTRADOR SET ");
        ArrayList<Object> alteracoes = new ArrayList<>();

        if (!original.getNome().equals(nome)){
            sql.append("NOME = ?, ");
            alteracoes.add(nome);
        }

        if (!original.getEmail().equals(email)){
            sql.append("EMAIL = ?, ");
            alteracoes.add(email);
        }

        if (!original.getFuncao().equals(funcao)){
            sql.append("FUNCAO = ?, ");
            alteracoes.add(funcao);
        }

        if (!original.getSenhaHash().equals(senhaHash)){
            sql.append("SENHA_HASH = ?, ");
            alteracoes.add(senhaHash);
        }

        if (alteracoes.isEmpty()) {
            return;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        alteracoes.add(original.getId());

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

        String sql = "DELETE FROM SUPER_ADMINISTRADOR WHERE id = ?";

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
