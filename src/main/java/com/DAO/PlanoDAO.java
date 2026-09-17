package com.DAO;

import com.model.Contrato;
import com.model.Plano;
import org.postgresql.util.PGInterval;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;

public class PlanoDAO extends DAO{

    public static final Map<String,String> camposFiltraveis = Map.of(
            "NOME", "Nome",
            "VALOR_MENSAL", "Valor Mensal",
            "DURACAO_MESES", "Duracao meses",
            "DESCRICAO","Descricao"
    );

    // Metodo que converte o valor de acordo com o campo que será filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "id","duracaoMeses" -> Integer.parseInt(valor);
                case "nome", "descricao" -> valor;
                case "valorMensal" -> Double.parseDouble(valor);
                default -> throw new IllegalArgumentException();
            };
        }catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    //construtor de DAO
    public PlanoDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    //insert
    public void cadastrar(Plano plano) throws SQLException{
        String nome = plano.getNome();
        Double valorMensal = plano.getValorMensal();
        Integer duracaoMeses = plano.getDuracaoMeses();
        String descricao = plano.getDescricao();

        String sql = """
                INSERT INTO PLANO (NOME, VALOR_MENSAL, DURACAO_MESES, DESCRICAO)
                VALUES(?, ?, ?, ?)
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, nome);
            pstmt.setDouble(2, valorMensal);
            pstmt.setInt(3, duracaoMeses);
            pstmt.setString(4,descricao);

            pstmt.execute();
            conn.commit();
        } catch (SQLException e){
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    //select
    public ArrayList<Plano> buscar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException{
        boolean temFiltro = true;

        ArrayList<Plano> resultado = new ArrayList<>();
        String sql = "SELECT ID, NOME, VALOR_MENSAL, DURACAO_MESES, DESCRICAO  FROM PLANO";

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
                    double valorMensal = rs.getDouble("VALOR_MENSAL");
                    int duracaoMeses = rs.getInt("DURACAO_MESES");
                    String descricao = rs.getString("DESCRICAO");

                    resultado.add(new Plano(id,nome, valorMensal, duracaoMeses, descricao));
                }
            }
        }

        conn.commit();
        return resultado;
    }

    //pesquisar por id
    public Plano pesquisarPorId(int id) throws SQLException{
        String sql = "SELECT NOME, VALOR_MENSAL, DESCRICAO, DURACAO_MESES FROM PLANO WHERE id = ?";

        Plano plano;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar o plano retorna null
                if (!rs.next()) {
                    return null;
                }

                String nome = rs.getString("NOME");
                double valorMensal = rs.getDouble("VALOR_MENSAL");
                String descricao = rs.getString("DESCRICAO");
                int duracaoMeses = rs.getInt("DURACAO_MESES");

                plano = new Plano(id, nome, valorMensal, duracaoMeses, descricao);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        conn.commit();
        return plano;
    }

    //pesquisar por nome
    public Plano pesquisarPorNome(String nome) throws SQLException{

        String sql = "SELECT ID, VALOR_MENSAL, DESCRICAO, DURACAO FROM PLANO WHERE NOME = ?";

        Plano plano;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se não encontrar o plano retorna null
                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("ID");
                double valorMensal = rs.getDouble("VALOR");
                String descricao = rs.getString("DESCRICAO");
                int duracaoMeses = rs.getInt("DURACAO_MESES");

                plano = new Plano(id, nome, valorMensal, duracaoMeses, descricao);
            }
        }

        conn.commit();
        return plano;
    }

    //alter
    public void atualizar(Plano original, Plano alterado) throws SQLException{
        String nome = alterado.getNome();
        double valorMensal = alterado.getValorMensal();
        String descricao = alterado.getDescricao();
        Integer duracaoMeses = alterado.getDuracaoMeses();

        StringBuilder sql = new StringBuilder("UPDATE PLANO SET ");
        ArrayList<Object> alteracoes = new ArrayList<>();

        if (!original.getNome().equals(nome)){
            sql.append("NOME = ?, ");
            alteracoes.add(nome);
        }

        if (original.getValorMensal() != valorMensal){
            sql.append("VALOR_MENSAL = ?, ");
            alteracoes.add(valorMensal);
        }

        if (!original.getDescricao().equals(descricao)){
            sql.append("DESCRICAO = ?, ");
            alteracoes.add(descricao);
        }

        if (original.getDuracaoMeses() != duracaoMeses){
            sql.append("DURACAO_MESES = ?, ");
            alteracoes.add(duracaoMeses);
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

        String sql = "DELETE FROM PLANO WHERE id = ?";

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
