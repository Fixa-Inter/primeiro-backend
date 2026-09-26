package com.DAO;

import com.model.Filtro;
import com.model.Instituicao;
import com.model.Pagamento;
import com.model.enums.MetodoPagamento;
import com.model.enums.OperacaoFiltro;

import java.math.BigDecimal;
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
            "ID", "ID",
            "VALOR", "Valor",
            "DATA_PAGAMENTO", "Data Pagamento",
            "FOI_REALIZADO", "Foi Realizado",
            "FK_CONTRATO_ID", "FK Contrato",
            "METODO_PAGAMENTO", "Metodo Pagamento"
    );

    public static final Map<String, List<OperacaoFiltro>> operacoesPorCampo = Map.of(
            "ID", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "VALOR", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.MAIOR_QUE,
                    OperacaoFiltro.MAIOR_OU_IGUAL,
                    OperacaoFiltro.MENOR_QUE,
                    OperacaoFiltro.MENOR_OU_IGUAL
            ),
            "DATA_PAGAMENTO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.MAIOR_QUE,
                    OperacaoFiltro.MAIOR_OU_IGUAL,
                    OperacaoFiltro.MENOR_QUE,
                    OperacaoFiltro.MENOR_OU_IGUAL
            ),
            "FOI_REALIZADO", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "FK_CONTRATO_ID", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "METODO_PAGAMENTO", List.of(
                    OperacaoFiltro.IGUAL
            )
    );

    // convertendo String recebida do Servlet
    public Object converterValor(String campo, String valor){

        try {
            return switch (campo) {
                case "id", "fk_contrato_id"-> Integer.parseInt(valor);
                case "valor" -> Float.parseFloat(valor);
                case "foi_realizado" -> Boolean.parseBoolean(valor);
                case "data_pagamento" -> LocalDate.parse(valor);
                case "metodo_pagamento" -> MetodoPagamento.converterEnum(valor);
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

        BigDecimal valor = pagamento.getValor();
        LocalDateTime dataPagamento = pagamento.getDataPagamento();
        Boolean foiRealizado = pagamento.getFoiRealizado();
        Integer fkContrato = pagamento.getFkContrato();
        Integer metodoPagamento = pagamento.getMetodoPagamento().getCodigo();
        String sql = """
                        INSERT INTO pagamento (VALOR, DATA_PAGAMENTO, FOI_REALIZADO, FK_CONTRATO_ID, METODO_PAGAMENTO)
                        VALUES (?, ?, ?, ?, ?)
                     """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setBigDecimal(1, valor);
            pstmt.setTimestamp(2, (dataPagamento == null ? null : Timestamp.valueOf(dataPagamento)));
            pstmt.setBoolean(3, foiRealizado);
            pstmt.setInt(4, fkContrato);
            pstmt.setInt(5, metodoPagamento);

            pstmt.execute();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // select
    public List<Pagamento> listarlistar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia) throws SQLException {

        List<Pagamento> resultado = new ArrayList<>();


        String sql = "SELECT ID, VALOR, DATA_PAGAMENTO, FOI_REALIZADO, FK_CONTRATO_ID, METODO_PAGAMENTO FROM pagamento";

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

        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)){
            sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);
        } else {
            sql += " ORDER BY ID ASC";
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

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){

                    int id = rs.getInt("id");
                    BigDecimal valor = rs.getBigDecimal("valor");
                    Timestamp data_pagamentoTimestamp = rs.getTimestamp("data_pagamento");
                    LocalDateTime data_pagamento = (data_pagamentoTimestamp == null ? null : data_pagamentoTimestamp.toLocalDateTime());
                    Boolean foiRealizado = rs.getBoolean("foi_realizado");
                    Integer fkContrato = rs.getInt("fk_contrato_id");
                    Integer metodoPagamento = rs.getInt("metodo_pagamento");

                    resultado.add(new Pagamento(id, valor, data_pagamento, foiRealizado, fkContrato, MetodoPagamento.converterEnum(metodoPagamento)));
                }

            }

        }

        conn.commit();
        return resultado;
    }

    // select id
    public Pagamento pesquisarId(int idPagamento) throws SQLException{

        String sql = "SELECT ID, VALOR, DATA_PAGAMENTO, FOI_REALIZADO, FK_CONTRATO_ID, METODO_PAGAMENTO FROM pagamento WHERE id = ?";

        Pagamento p;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, idPagamento);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao procurar instituicao");
                }

                int id = rs.getInt("id");
                BigDecimal valor = rs.getBigDecimal("valor");
                Timestamp data_pagamentoTimestamp = rs.getTimestamp("data_pagamento");
                LocalDateTime data_pagamento = (data_pagamentoTimestamp == null ? null : data_pagamentoTimestamp.toLocalDateTime());
                Boolean foiRealizado = rs.getBoolean("foi_realizado");
                Integer fkContrato = rs.getInt("fk_contrato_id");
                Integer metodoPagamento = rs.getInt("metodo_pagamento");

                p = new Pagamento(id, valor, data_pagamento, foiRealizado, fkContrato, MetodoPagamento.converterEnum(metodoPagamento));
            }
        }

        conn.commit();
        return p;
    }


    // update
    public void atualizar(Pagamento original, Pagamento alterado) throws SQLException{

        int id = alterado.getId();
        BigDecimal valor = alterado.getValor();
        LocalDateTime dataPagamento = alterado.getDataPagamento();
        Boolean foiRealizado = alterado.getFoiRealizado();
        Integer fkContrato = alterado.getFkContrato();
        Integer metodoPagamento = alterado.getMetodoPagamento().getCodigo();

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

        if(!Objects.equals(metodoPagamento, original.getMetodoPagamento().getCodigo())){
            sql.append("metodo_pagamento = ?, ");
            valores.add(metodoPagamento);
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