package com.DAO;

import com.model.Instituicao;
import com.model.Pagamento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PagamentoDAO extends DAO{

    // map dos campos que sao filtraveis
    public static final Map<String, String> camposFiltraveis = Map.of(

            "id", "ID",
            "valor", "Valor",
            "data_pagamento", "Data Pagamento",
            "foi_realizado", "Foi Realizado",
            "fk_contrato_id", "FK Contrato",
            "fk_metodo_pagamento_id", "FK Metodo Pagamento"
    );

    // convertendo String recebida do Servlet
    public Object converterValor(String campo, String valor){

        try {
            return switch (campo) {
                case "id", "fk_contrato_id", "fk_metodo_pagamento_id" -> Integer.parseInt(valor);
                case "valor" -> Float.parseFloat(valor);
                case "foi_realizado" -> Boolean.parseBoolean(valor);
                case "data_pagamento" -> LocalDate.parse(valor);
                default -> throw new IllegalArgumentException();
            };
        }  catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }

    }

    // construtor
    public PagamentoDAO() throws SQLException, ClassNotFoundException{
        super();
    }

    // insert

    public void cadastrar(Pagamento pagamento) throws SQLException {

        float valor = pagamento.getValor();
        LocalDateTime dataPagamento = pagamento.getDataPagamento();
        Boolean foiRealizado = pagamento.getFoiRealizado();
        Integer fkContrato = pagamento.getFkContrato();
        Integer fkMetodoPagamento = pagamento.getFkMetodoPagamento();

        String sql = """
                        INSERT INTO pagamento (VALOR, DATA_PAGAMENTO, FOI_REALIZADO, FK_CONTRATO_ID, FK_METODO_PAGAMENTO_ID)
                        VALUES (?, ?, ?, ?, ?)
                     """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setFloat(1, valor);
            pstmt.setTimestamp(2, (dataPagamento == null ? null : Timestamp.valueOf(dataPagamento)));
            pstmt.setBoolean(3, foiRealizado);
            pstmt.setInt(4, fkContrato);
            pstmt.setInt(5, fkMetodoPagamento);

            pstmt.execute();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // select

    public List<Pagamento> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {

        boolean temFiltro = true;

        List<Pagamento> pagamentos = new ArrayList<>();


        String sql = "SELECT id, valor, data_pagamento, foi_realizado, fk_contrato_id, fk_metodo_pagamento_id FROM pagamento";

        if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)){
            sql += " WHERE %s = ?".formatted(campoFiltro);
        } else {
            temFiltro = false;
        }

        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)){
            sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);
        } else {
            sql += " ORDER BY ID ASC";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            if (temFiltro){
                pstmt.setObject(1, valorFiltro);
            }

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){

                    int id = rs.getInt("id");
                    float valor = rs.getFloat("valor");
                    Timestamp data_pagamentoTimestamp = rs.getTimestamp("data_pagamento");
                    LocalDateTime data_pagamento = (data_pagamentoTimestamp == null ? null : data_pagamentoTimestamp.toLocalDateTime());
                    Boolean foiRealizado = rs.getBoolean("foi_realizado");
                    Integer fkContrato = rs.getInt("fk_contrato_id");
                    Integer fkMetodoPagamento = rs.getInt("fk_metodo_pagamento_id");

                    pagamentos.add(new Pagamento(id, valor, data_pagamento, foiRealizado, fkContrato, fkMetodoPagamento));
                }

            }

        }

        conn.commit();
        return pagamentos;
    }

    // select id
    public Pagamento pesquisarId(int idPagamento) throws SQLException{

        String sql = "SELECT id, valor, data_pagamento, foi_realizado, fk_contrato_id, fk_metodo_pagamento_id FROM pagamento WHERE id = ?";

        Pagamento p;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, idPagamento);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao procurar instituicao");
                }

                int id = rs.getInt("id");
                float valor = rs.getFloat("valor");
                Timestamp data_pagamentoTimestamp = rs.getTimestamp("data_pagamento");
                LocalDateTime data_pagamento = (data_pagamentoTimestamp == null ? null : data_pagamentoTimestamp.toLocalDateTime());
                Boolean foiRealizado = rs.getBoolean("foi_realizado");
                Integer fkContrato = rs.getInt("fk_contrato_id");
                Integer fkMetodoPagamento = rs.getInt("fk_metodo_pagamento_id");

                p = new Pagamento(id, valor, data_pagamento, foiRealizado, fkContrato, fkMetodoPagamento);
            }
        }

        conn.commit();
        return p;
    }


    // update
    public void atualizar(Pagamento original, Pagamento alterado) throws SQLException{

        int id = alterado.getId();
        float valor = alterado.getValor();
        LocalDateTime dataPagamento = alterado.getDataPagamento();
        Boolean foiRealizado = alterado.getFoiRealizado();
        Integer fkContrato = alterado.getFkContrato();
        Integer fkMetodoPagamento = alterado.getFkMetodoPagamento();

        StringBuilder sql = new StringBuilder("UPDATE pagamento SET ");
        List<Object> valores = new ArrayList<>();


        if(!Objects.equals(valor, original.getValor())){
            sql.append("valor = ?, ");
            valores.add(valor);
        }

        if(!Objects.equals(dataPagamento, original.getDataPagamento())){
            sql.append("data_pagamento = ?, ");
            valores.add(dataPagamento);
        }

        if(!Objects.equals(foiRealizado, original.getFoiRealizado())){
            sql.append("foi_realizado = ?, ");
            valores.add(foiRealizado);
        }

        if(!Objects.equals(fkContrato, original.getFkContrato())){
            sql.append("fk_contrato_id = ?, ");
            valores.add(fkContrato);
        }

        if(!Objects.equals(fkMetodoPagamento, original.getFkMetodoPagamento())){
            sql.append("fk_metodo_pagamento_id = ?, ");
            valores.add(fkMetodoPagamento);
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

        String sql = "DELETE FROM pagamento WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            conn.commit();
        } catch (SQLException e){
            conn.rollback();
            throw e;
        }
    }
}