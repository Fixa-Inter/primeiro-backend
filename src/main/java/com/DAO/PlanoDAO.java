package com.DAO;

import com.model.Contrato;
import com.model.Filtro;
import com.model.Plano;
import com.model.enums.OperacaoFiltro;
import com.utils.desfazer.DesfazerDelete;
import com.utils.desfazer.DesfazerDeleteFactory;
import com.utils.desfazer.DesfazerInsert;
import com.utils.desfazer.DesfazerUpdate;
import com.utils.desfazer.GerenciadorDesfazer;
import jakarta.servlet.http.HttpServletRequest;
import org.postgresql.util.PGInterval;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Acesso ao banco para a tabela de planos
public class PlanoDAO extends DAO{

    public static final Map<String,String> camposFiltraveis = Map.of(
            "ID", "ID",
            "NOME", "Nome",
            "VALOR_MENSAL", "Valor Mensal",
            "DURACAO_MESES", "Duracao meses",
            "DESCRICAO","Descricao"
    );

    public static final Map<String, List<OperacaoFiltro>> operacoesPorCampo = Map.of(
            "ID", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "NOME", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),

            "VALOR_MENSAL", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.MAIOR_QUE,
                    OperacaoFiltro.MAIOR_OU_IGUAL,
                    OperacaoFiltro.MENOR_QUE,
                    OperacaoFiltro.MENOR_OU_IGUAL
            ),

            "DURACAO_MESES", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.MAIOR_QUE,
                    OperacaoFiltro.MAIOR_OU_IGUAL,
                    OperacaoFiltro.MENOR_QUE,
                    OperacaoFiltro.MENOR_OU_IGUAL
            ),

            "DESCRICAO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            )
    );

    // Converte o valor para o tipo do campo filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "ID","DURACAO_MESES" -> Integer.parseInt(valor);
                case "NOME", "DESCRICAO" -> valor;
                case "VALOR_MENSAL" -> Double.parseDouble(valor);
                default -> throw new IllegalArgumentException();
            };
        }catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    // Abre a conexão com o banco
    public PlanoDAO() throws SQLException {
        super();
    }

    // Insere um plano e devolve o id gerado
    public int cadastrar(Plano plano) throws SQLException{
        String nome = plano.getNome();
        Double valorMensal = plano.getValorMensal();
        Integer duracaoMeses = plano.getDuracaoMeses();
        String descricao = plano.getDescricao();

        String sql = """
                INSERT INTO PLANO (NOME, VALOR_MENSAL, DURACAO_MESES, DESCRICAO)
                VALUES(?, ?, ?, ?) RETURNING ID
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, nome);
            pstmt.setDouble(2, valorMensal);
            pstmt.setInt(3, duracaoMeses);
            pstmt.setString(4,descricao);

            int id;
            try (ResultSet rs = pstmt.executeQuery()) { rs.next(); id = rs.getInt("ID"); }
            conn.commit();
            return id;
        } catch (SQLException e){
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    // Insere um plano e registra a ação para desfazer
    public int cadastrar(Plano plano, HttpServletRequest request) throws SQLException {
        int id = cadastrar(plano);
        GerenciadorDesfazer.registrarComando(request, new DesfazerInsert(getClass(), id));
        return id;
    }

    // Insere um plano com o id original, para desfazer uma exclusão
    public int cadastrarCompleto(Plano plano) throws SQLException {
        String sql = "INSERT INTO plano (id, nome, valor_mensal, duracao_meses, descricao, data_criacao) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, plano.getId());
            pstmt.setString(2, plano.getNome());
            pstmt.setObject(3, plano.getValorMensal());
            pstmt.setObject(4, plano.getDuracaoMeses());
            pstmt.setString(5, plano.getDescricao());
            pstmt.setObject(6, plano.getDataCriacao());
            pstmt.executeUpdate();
            try (PreparedStatement serial = conn.prepareStatement("SELECT setval(pg_get_serial_sequence('plano', 'id'), COALESCE(max(id), 1)) FROM plano")) { serial.executeQuery(); }
            conn.commit();
            return plano.getId();
        } catch (SQLException e) { conn.rollback(); throw e; }
    }

    // Lista planos, aplicando filtros, ordenação e pesquisa
    public List<Plano> buscar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia, String pesquisa) throws SQLException{

        ArrayList<Plano> resultado = new ArrayList<>();
        String sql = "SELECT ID, NOME, VALOR_MENSAL, DURACAO_MESES, DESCRICAO, DATA_CRIACAO FROM PLANO";

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
                    double valorMensal = rs.getDouble("VALOR_MENSAL");
                    int duracaoMeses = rs.getInt("DURACAO_MESES");
                    String descricao = rs.getString("DESCRICAO");
                    Timestamp dataCriacaoSQL = rs.getTimestamp("data_criacao");
                    LocalDateTime dataCriacao = (dataCriacaoSQL == null
                            ? null
                            : dataCriacaoSQL.toLocalDateTime());

                    resultado.add(new Plano(id,nome, valorMensal, duracaoMeses, descricao, dataCriacao));
                }
            }
        }

        if (pesquisa != null && !pesquisa.isEmpty()) {
            String pesquisaNormalizada = pesquisa.toLowerCase().trim().replace(" ", "");
            resultado.removeIf(plano -> !plano.toString()
                    .toLowerCase().trim().replace(" ", "")
                    .contains(pesquisaNormalizada));
        }

        conn.commit();
        return resultado;
    }

    // Busca um plano pelo id
    public Plano pesquisarPorId(int id) throws SQLException{
        String sql = "SELECT NOME, VALOR_MENSAL, DESCRICAO, DURACAO_MESES, DATA_CRIACAO FROM PLANO WHERE id = ?";

        Plano plano;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Plano não encontrado
                if (!rs.next()) {
                    return null;
                }

                String nome = rs.getString("NOME");
                double valorMensal = rs.getDouble("VALOR_MENSAL");
                String descricao = rs.getString("DESCRICAO");
                int duracaoMeses = rs.getInt("DURACAO_MESES");
                Timestamp dataCriacaoSQL = rs.getTimestamp("DATA_CRIACAO");
                LocalDateTime dataCriacao = (dataCriacaoSQL == null
                        ? null
                        : dataCriacaoSQL.toLocalDateTime());

                plano = new Plano(id, nome, valorMensal, duracaoMeses, descricao, dataCriacao);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        conn.commit();
        return plano;
    }

    // Atualiza um plano, só nos campos que mudaram
    public void atualizar(Plano original, Plano alterado) throws SQLException{
        String nome = alterado.getNome();
        double valorMensal = alterado.getValorMensal();
        String descricao = alterado.getDescricao();
        Integer duracaoMeses = alterado.getDuracaoMeses();
        LocalDateTime dataCriacao = alterado.getDataCriacao();

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

    // Atualiza um plano e registra a ação para desfazer
    public void atualizar(Plano original, Plano alterado, HttpServletRequest request) throws SQLException {
        atualizar(original, alterado);
        GerenciadorDesfazer.registrarComando(request, new DesfazerUpdate(getClass(), original));
    }

    // Exclui um plano pelo id
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

    // Exclui um plano e registra a ação para desfazer
    public void remover(int id, HttpServletRequest request) throws SQLException {
        DesfazerDelete comando = DesfazerDeleteFactory.criar(getClass(), id);
        remover(id);
        GerenciadorDesfazer.registrarComando(request, comando);
    }

}
