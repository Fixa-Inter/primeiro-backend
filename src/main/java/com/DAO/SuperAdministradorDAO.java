package com.DAO;

import com.model.Filtro;
import com.model.SuperAdministrador;
import com.model.enums.OperacaoFiltro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuperAdministradorDAO extends DAO{
    public static final Map<String,String> camposFiltraveis = Map.of(
            "ID", "ID",
            "NOME", "Nome",
            "EMAIL", "Email"
    );

    public static final Map<String, List<OperacaoFiltro>> operacoesPorCampo = Map.of(
            "ID", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "NOME", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "EMAIL", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            )
    );

    // Metodo que converte o valor de acordo com o campo que será filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "id" -> Integer.parseInt(valor);
                case "nome", "email" -> valor;
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
        String nome = superAdministrador.getNome();
        String senhaHash = superAdministrador.getSenhaHash();
        String email = superAdministrador.getEmail();

        String sql = """
                INSERT INTO SUPER_ADMINISTRADOR (NOME,SENHA_HASH,EMAIL) 
                VALUES(?,?,?)
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,nome);
            pstmt.setString(2,senhaHash);
            pstmt.setString(3,email);

            pstmt.execute();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    //select
    public List<SuperAdministrador> buscar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia) throws SQLException{

        ArrayList<SuperAdministrador> resultado = new ArrayList<>();
        String sql = "SELECT ID, NOME, EMAIL FROM SUPER_ADMINISTRADOR";

        if (filtros != null && !filtros.isEmpty()) {

            sql += " WHERE ";

            for (int i = 0; i < filtros.size(); i++) {

                Filtro filtro = filtros.get(i);

                // Verifica se o campo existe
                if (!camposFiltraveis.containsKey(filtro.getCampoFiltravel())) {
                    throw new IllegalArgumentException("Campo inválido: " + filtro.getCampoFiltravel());
                }

                // Verifica se a operação é permitida para esse campo
                if (!operacoesPorCampo
                        .get(filtro.getCampoFiltravel())
                        .contains(filtro.getOperacaoFiltro())) {

                    throw new IllegalArgumentException(
                            "Operação inválida para o campo: " + filtro.getCampoFiltravel()
                    );
                }

                // Coloca AND a partir do segundo filtro
                if (i > 0) {
                    sql += " AND ";
                }

                // Adiciona a condição
                if (filtro.getOperacaoFiltro() == OperacaoFiltro.CONTEM) {

                    sql += "REPLACE(unaccent(" + filtro.getCampoFiltravel() + "), ' ', '') "
                            + filtro.getOperacaoFiltro().getOperadorSQL()
                            + " REPLACE(unaccent(?), ' ', '')";

                } else {

                    sql += filtro.getCampoFiltravel() + " "
                            + filtro.getOperacaoFiltro().getOperadorSQL() + " ?";
                }
            }
        }

        // Verificando campo e direcao da ordenação
        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)) {
            sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);
        } else {
            sql += " ORDER BY id ASC";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Verifica se tem filtro, se sim define a variável do comando SQL
            if (filtros != null && !filtros.isEmpty()) {
                for (int i = 0; i < filtros.size(); i++) {
                    Filtro filtro = filtros.get(i);

                    if (filtro.getOperacaoFiltro() == OperacaoFiltro.CONTEM) {
                        pstmt.setObject(i + 1, "%" + filtro.getValor() + "%");
                    } else {
                        pstmt.setObject(i + 1, filtro.getValor());
                    }
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String nome = rs.getString("NOME");
                    String email = rs.getString("EMAIL");

                    resultado.add(new SuperAdministrador(id, nome, null, email));
                }
            }
        }

        conn.commit();
        return resultado;
    }

    //pesquisar por id
    public SuperAdministrador pesquisarPorId(int id) throws SQLException {
        String sql = "SELECT NOME, EMAIL FROM SUPER_ADMINISTRADOR WHERE id = ?";
        SuperAdministrador superAdministrador;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String nome = rs.getString("NOME");
                String email = rs.getString("EMAIL");
                String senhaHash = rs.getString("SENHA_HASH");

                superAdministrador = new SuperAdministrador(id, nome, senhaHash, email);
            }
        }catch (SQLException e) {
            throw new RuntimeException();
        }
        return superAdministrador;
    }

    //pesquisar por email
    public SuperAdministrador pesquisarPorEmail(String email){
        {
            String sql = "SELECT NOME, EMAIL FROM SUPER_ADMINISTRADOR WHERE EMAIL = ?";
            SuperAdministrador superAdministrador;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    int id = rs.getInt("ID");
                    String nome = rs.getString("NOME");
                    String senhaHash = rs.getString("SENHA_HASH");

                    superAdministrador = new SuperAdministrador(id, nome, senhaHash, email);
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
