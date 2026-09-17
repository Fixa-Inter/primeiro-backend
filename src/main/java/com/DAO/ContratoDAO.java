package com.DAO;

import com.model.Contrato;
import com.model.Plano;
import com.model.SuperAdministrador;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;

public class ContratoDAO extends DAO{
    public static final Map<String,String> camposFiltraveis = Map.of(
            "DATA_INICIO", "Data de inicio",
            "DATA_VENCIMENTO", "Data de vencimento",
            "FK_INSTITUICAO_ID", "fkInstituicao",
            "FK_PLANO_ID","fkPlano",
            "ESTA_VIGENTE","staVigente"
    );

    // Metodo que converte o valor de acordo com o campo que será filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "id","fkInstituicao","fkPlano" -> Integer.parseInt(valor);
                case "nome", "funcao", "email" -> valor;
                case "estaVigente" -> Boolean.parseBoolean(valor);
                case "dataInicio","dataVencimento" -> LocalDate.parse(valor);
                default -> throw new IllegalArgumentException();
            };
        }catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    //construtor de DAO
    public ContratoDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    //insert
    public void cadastrar(Contrato contrato) throws SQLException{
        LocalDate dataInicio = contrato.getDataInicio();
        LocalDate dataVencimento = contrato.getDataVencimento();
        int fkInstituicao = contrato.getFkInstituicao();
        int fkPlano = contrato.getFkPlano();
        Boolean estaVigente = contrato.getEstaVigente();

        String sql = """
                INSERT INTO CONTRATO (DATA_INICIO,DATA_VENCIMENTO,FK_INSTITUICAO_ID,FK_PLANO_ID,ESTA_VIGENTE) 
                VALUES(?,?,?,?,?)
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1,dataInicio, Types.DATE);
            pstmt.setObject(2,dataVencimento, Types.DATE);
            pstmt.setInt(3,fkInstituicao);
            pstmt.setInt(4,fkPlano);
            pstmt.setBoolean(5,estaVigente);

            pstmt.execute();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    //select
    public ArrayList<Contrato> buscar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException{
        boolean temFiltro = true;

        ArrayList<Contrato> resultado = new ArrayList<>();
        String sql = "SELECT ID, DATA_INICIO, DATA_VENCIMENTO, FK_INSTITUICAO_ID, FK_PLANO_ID, ESTA_VIGENTE FROM CONTRATO";

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
                    Date dataInicioSQL = rs.getDate("data_inicio");
                    LocalDate dataInicio = (dataInicioSQL == null ? null : dataInicioSQL.toLocalDate());
                    Date dataVencimentoSQL = rs.getDate("data_vencimento");
                    LocalDate dataVencimento = (dataVencimentoSQL == null ? null : dataVencimentoSQL.toLocalDate());
                    int fkInstituicao = rs.getInt("FK_INSTITUICAO_ID");
                    int fkPlano = rs.getInt("FK_PLANO_ID");
                    Boolean estaVigente = rs.getBoolean("ESTA_VIGENTE");

                    resultado.add(new Contrato(id,dataInicio, dataVencimento, fkInstituicao, fkPlano, estaVigente));
                }
            }
        }

        conn.commit();
        return resultado;
    }

    //pesquisar por id
    public Contrato pesquisarPorId(int id) throws SQLException {
        String sql = "SELECT DATA_INICIO,DATA_VENCIMENTO,FK_INSTITUICAO_ID,FK_PLANO_ID,ESTA_VIGENTE FROM CONTRATO WHERE id = ?";
        Contrato contrato;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                LocalDate dataInicio = rs.getObject("DATA_INICIO", LocalDate.class);
                LocalDate dataVencimento = rs.getObject("DATA_VENCIMENTO", LocalDate.class);
                int fkInstituicao = rs.getInt("FK_INSTITUICAO_ID");
                int fkPlano = rs.getInt("FK_PLANO_ID");
                Boolean estaVigente = rs.getBoolean("ESTA_VIGENTE");

                contrato = new Contrato(id, dataInicio, dataVencimento, fkPlano, fkInstituicao, estaVigente);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return contrato;
    }

    //pesquisar por fkInstituicao
    public Contrato pesquisarPorFkInstituicao(int fkInstituicao) throws SQLException {
        String sql = "SELECT DATA_INICIO,DATA_VENCIMENTO,FK_INSTITUICAO_ID,FK_PLANO_ID,ESTA_VIGENTE FROM CONTRATO WHERE FK_INSTITUICAO_ID = ?";
        Contrato contrato;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, fkInstituicao);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("ID");
                LocalDate dataInicio = rs.getObject("DATA_INICIO", LocalDate.class);
                LocalDate dataVencimento = rs.getObject("DATA_VENCIMENTO", LocalDate.class);
                int fkPlano = rs.getInt("FK_PLANO_ID");
                Boolean estaVigente = rs.getBoolean("ESTA_VIGENTE");

                contrato = new Contrato(id, dataInicio, dataVencimento, fkPlano, fkInstituicao, estaVigente);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return contrato;
    }

    //alter
    public void atualizar(Contrato original, Contrato alterado) throws SQLException{
        Integer id = alterado.getId();
        LocalDate dataInicio = alterado.getDataInicio();
        LocalDate dataVencimento = alterado.getDataVencimento();
        int fkInstituicao = alterado.getFkInstituicao();
        int fkPlano = alterado.getFkPlano();
        Boolean estaVigente = alterado.getEstaVigente();

        StringBuilder sql = new StringBuilder("UPDATE CONTRATO SET ");
        ArrayList<Object> alteracoes = new ArrayList<>();

        if (!original.getDataInicio().isEqual(dataInicio)){
            sql.append("DATA_INICIO = ?, ");
            alteracoes.add(dataInicio);
        }

        if (!original.getDataVencimento().isEqual(dataVencimento)){
            sql.append("DATA_VENCIMENTO = ?, ");
            alteracoes.add(dataVencimento);
        }

        if (original.getFkInstituicao() != fkInstituicao){
            sql.append("FK_INSTITUICAO_ID = ?, ");
            alteracoes.add(fkInstituicao);
        }

        if (original.getFkPlano() != fkPlano){
            sql.append("FK_PLANO_ID = ?, ");
            alteracoes.add(fkPlano);
        }

        if (original.getEstaVigente() != estaVigente){
            sql.append("ESTA_VIGENTE = ?, ");
            alteracoes.add(estaVigente);
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

        String sql = "DELETE FROM CONTRATO WHERE id = ?";

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
