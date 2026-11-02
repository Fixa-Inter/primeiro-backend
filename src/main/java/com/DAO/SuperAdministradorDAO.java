package com.DAO;

import com.model.Filtro;
import com.model.SuperAdministrador;
import com.model.enums.OperacaoFiltro;
import com.utils.desfazer.DesfazerDelete;
import com.utils.desfazer.DesfazerDeleteFactory;
import com.utils.desfazer.DesfazerInsert;
import com.utils.desfazer.DesfazerUpdate;
import com.utils.desfazer.GerenciadorDesfazer;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Acesso ao banco para a tabela de super administradores
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

    // Converte o valor para o tipo do campo filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "ID" -> Integer.parseInt(valor);
                case "NOME", "EMAIL" -> valor;
                default -> throw new IllegalArgumentException();
            };
        }catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    // Abre a conexão com o banco
    public SuperAdministradorDAO() throws SQLException {
        super();
    }

    // Insere um super administrador e devolve o id gerado
    public int cadastrar(SuperAdministrador superAdministrador) throws SQLException{
        String nome = superAdministrador.getNome();
        String senhaHash = superAdministrador.getSenhaHash();
        String email = superAdministrador.getEmail();

        String sql = """
                INSERT INTO SUPER_ADMINISTRADOR (NOME,SENHA_HASH,EMAIL) 
                VALUES(?,?,?) RETURNING ID
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,nome);
            pstmt.setString(2,senhaHash);
            pstmt.setString(3,email);

            int id;
            try (ResultSet rs = pstmt.executeQuery()) { rs.next(); id = rs.getInt("ID"); }
            conn.commit();
            return id;
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    // Insere um super administrador e registra a ação para desfazer
    public int cadastrar(SuperAdministrador superAdministrador, HttpServletRequest request) throws SQLException {
        int id = cadastrar(superAdministrador);
        GerenciadorDesfazer.registrarComando(request, new DesfazerInsert(getClass(), id));
        return id;
    }

    // Insere um super administrador com o id original, para desfazer uma exclusão
    public int cadastrarCompleto(SuperAdministrador superAdministrador) throws SQLException {
        String sql = "INSERT INTO super_administrador (id, nome, senha_hash, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, superAdministrador.getId());
            pstmt.setString(2, superAdministrador.getNome());
            pstmt.setString(3, superAdministrador.getSenhaHash());
            pstmt.setString(4, superAdministrador.getEmail());
            pstmt.executeUpdate();
            try (PreparedStatement serial = conn.prepareStatement("SELECT setval(pg_get_serial_sequence('super_administrador', 'id'), COALESCE(max(id), 1)) FROM super_administrador")) { serial.executeQuery(); }
            conn.commit();
            return superAdministrador.getId();
        } catch (SQLException e) { conn.rollback(); throw e; }
    }

    // Lista super administradores, aplicando filtros, ordenação e pesquisa
    public List<SuperAdministrador> buscar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia, String pesquisa) throws SQLException{

        ArrayList<SuperAdministrador> resultado = new ArrayList<>();
        String sql = "SELECT ID, NOME, EMAIL FROM SUPER_ADMINISTRADOR";

        if (filtros != null && !filtros.isEmpty()) {

            sql += " WHERE ";

            for (int i = 0; i < filtros.size(); i++) {

                Filtro filtro = filtros.get(i);

                // Valida se o campo é filtrável
                if (!camposFiltraveis.containsKey(filtro.getCampoFiltravel())) {
                    throw new IllegalArgumentException("Campo inválido: " + filtro.getCampoFiltravel());
                }

                // Valida se a operação é permitida para o campo
                if (!operacoesPorCampo
                        .get(filtro.getCampoFiltravel())
                        .contains(filtro.getOperacaoFiltro())) {

                    throw new IllegalArgumentException(
                            "Operação inválida para o campo: " + filtro.getCampoFiltravel()
                    );
                }

                // Liga os filtros com AND a partir do segundo
                if (i > 0) {
                    sql += " AND ";
                }

                // Adiciona a condição do filtro
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

        // Define a ordenação, com id crescente como padrão
        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)) {
            sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);
        } else {
            sql += " ORDER BY id ASC";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Preenche os parâmetros do SQL com os valores dos filtros
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

        if (pesquisa != null && !pesquisa.isEmpty()) {
            String pesquisaNormalizada = pesquisa.toLowerCase().trim().replace(" ", "");
            resultado.removeIf(superAdministrador -> !superAdministrador.toString()
                    .toLowerCase().trim().replace(" ", "")
                    .contains(pesquisaNormalizada));
        }

        conn.commit();
        return resultado;
    }

    // Busca um super administrador pelo id
    public SuperAdministrador pesquisarPorId(int id) throws SQLException {
        String sql = "SELECT NOME, EMAIL, SENHA_HASH FROM SUPER_ADMINISTRADOR WHERE id = ?";
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

    // Busca o super administrador pelo e-mail
    public SuperAdministrador pesquisarPorEmail(String email){
        {
            String sql = "SELECT ID, NOME, EMAIL, SENHA_HASH FROM SUPER_ADMINISTRADOR WHERE EMAIL = ?";
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

    // Atualiza um super administrador, só nos campos que mudaram
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

        if (senhaHash != null && !senhaHash.equals(original.getSenhaHash())){
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

    // Atualiza um super administrador e registra a ação para desfazer
    public void atualizar(SuperAdministrador original, SuperAdministrador alterado, HttpServletRequest request) throws SQLException {
        atualizar(original, alterado);
        GerenciadorDesfazer.registrarComando(request, new DesfazerUpdate(getClass(), original));
    }

    // Exclui um super administrador pelo id
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

    // Exclui um super administrador e registra a ação para desfazer
    public void remover(int id, HttpServletRequest request) throws SQLException {
        DesfazerDelete comando = DesfazerDeleteFactory.criar(getClass(), id);
        remover(id);
        GerenciadorDesfazer.registrarComando(request, comando);
    }

}
