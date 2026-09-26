package com.DAO;

import com.model.Contrato;
import com.model.Filtro;
import com.model.Plano;
import com.model.SuperAdministrador;
import com.model.enums.MetodoPagamento;
import com.model.enums.OperacaoFiltro;
import com.model.enums.StatusContrato;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContratoDAO extends DAO{
    public static final Map<String,String> camposFiltraveis = Map.of(
            "ID", "ID",
            "DATA_INICIO", "Data de inicio",
            "DATA_VENCIMENTO", "Data de vencimento",
            "FK_ENDERECO_ID", "fkEndereco",
            "FK_PLANO_ID","fkPlano",
            "STATUS_CONTRATO","statusContrato"
    );

    public static final Map<String, List<OperacaoFiltro>> operacoesPorCampo = Map.of(
            "ID", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "DATA_INICIO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.MAIOR_QUE,
                    OperacaoFiltro.MAIOR_OU_IGUAL,
                    OperacaoFiltro.MENOR_QUE,
                    OperacaoFiltro.MENOR_OU_IGUAL
            ),
            "DATA_VENCIMENTO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.MAIOR_QUE,
                    OperacaoFiltro.MAIOR_OU_IGUAL,
                    OperacaoFiltro.MENOR_QUE,
                    OperacaoFiltro.MENOR_OU_IGUAL
            ),
            "FK_ENDERECO_ID", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "FK_PLANO_ID", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "STATUS_CONTRATO",List.of(
                    OperacaoFiltro.IGUAL
            )
    );

    // Metodo que converte o valor de acordo com o campo que será filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "FK_ENDERECO_ID", "FK_PLANO_ID" -> Integer.parseInt(valor);
                case "DATA_INICIO", "DATA_VENCIMENTO" -> LocalDate.parse(valor);
                case "STATUS_CONTRATO" -> StatusContrato.converterEnum(valor);
                default -> throw new IllegalArgumentException();
            };
        } catch (DateTimeParseException |
                 IllegalArgumentException |
                 NullPointerException e) {
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
        Integer fkEndereco = contrato.getFkEndereco();
        Integer fkPlano = contrato.getFkPlano();
        Integer statusContrato = contrato.getStatusContrato().getCodigo();

        String sql = """
                INSERT INTO CONTRATO (DATA_VENCIMENTO,FK_PLANO_ID,FK_ENDERECO_ID, STATUS_CONTRATO) 
                VALUES(?,?,?,?)
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1,dataVencimento, Types.DATE);
            pstmt.setInt(2,fkPlano);
            pstmt.setInt(3,fkEndereco);
            pstmt.setObject(4,statusContrato);

            // tira as colunas data_criacao e data_inicio

            pstmt.execute();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    //select
    public List<Contrato> buscar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia) throws SQLException{

        ArrayList<Contrato> resultado = new ArrayList<>();
        String sql = "SELECT ID, DATA_INICIO, DATA_VENCIMENTO, FK_ENDERECO_ID, FK_PLANO_ID, STATUS_CONTRATO FROM CONTRATO";

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
                sql += filtro.getCampoFiltravel() + " " +filtro.getOperacaoFiltro().getOperadorSQL() + " ?";
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
                    Date dataInicioSQL = rs.getDate("data_inicio");
                    LocalDate dataInicio = (dataInicioSQL == null ? null : dataInicioSQL.toLocalDate());
                    Date dataVencimentoSQL = rs.getDate("data_vencimento");
                    LocalDate dataVencimento = (dataVencimentoSQL == null ? null : dataVencimentoSQL.toLocalDate());
                    int fkEndereco = rs.getInt("FK_ENDERECO_ID");
                    int fkPlano = rs.getInt("FK_PLANO_ID");
                    int statusContrato = rs.getInt("STATUS_CONTRATO");

                    resultado.add(new Contrato(id,dataInicio, dataVencimento, fkEndereco, fkPlano, StatusContrato.converterEnum(statusContrato)));
                }
            }
        }

        conn.commit();
        return resultado;
    }

    //pesquisar por id
    public Contrato pesquisarPorId(int id) throws SQLException {
        String sql = "SELECT DATA_INICIO,DATA_VENCIMENTO,FK_ENDERECO_ID,FK_PLANO_ID,STATUS_CONTRATO FROM CONTRATO WHERE id = ?";
        Contrato contrato;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                LocalDate dataInicio = rs.getObject("DATA_INICIO", LocalDate.class);
                LocalDate dataVencimento = rs.getObject("DATA_VENCIMENTO", LocalDate.class);
                int fkEndereco = rs.getInt("FK_ENDERECO_ID");
                int fkPlano = rs.getInt("FK_PLANO_ID");
                int statusContrato = rs.getInt("STATUS_CONTRATO");

                contrato = new Contrato(id, dataInicio, dataVencimento, fkPlano, fkEndereco, StatusContrato.converterEnum(statusContrato));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return contrato;
    }

    //pesquisar por fkEndereco
    public Contrato pesquisarPorFkEndereco(int fkEndereco) throws SQLException {
        String sql = "SELECT DATA_INICIO,DATA_VENCIMENTO,FK_ENDERECO_ID,FK_PLANO_ID,STATUS_CONTRATO FROM CONTRATO WHERE FK_ENDERECO_ID = ?";
        Contrato contrato;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, fkEndereco);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("id");
                LocalDate dataInicio = rs.getObject("DATA_INICIO", LocalDate.class);
                LocalDate dataVencimento = rs.getObject("DATA_VENCIMENTO", LocalDate.class);
                int fkPlano = rs.getInt("FK_PLANO_ID");
                int statusContrato = rs.getInt("STATUS_CONTRATO");

                contrato = new Contrato(id, dataInicio, dataVencimento, fkPlano, fkEndereco, StatusContrato.converterEnum(statusContrato));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return contrato;
    }

    //alter
    public void atualizar(Contrato original, Contrato alterado) throws SQLException{

        Integer id = alterado.getId();
        LocalDate dataVencimento = alterado.getDataVencimento();
        // data_inicio pode ser alterada? consideramos que nao
        int fkEndereco = alterado.getFkEndereco();
        int fkPlano = alterado.getFkPlano();
        Integer statusContrato = alterado.getStatusContrato().getCodigo();

        StringBuilder sql = new StringBuilder("UPDATE CONTRATO SET ");
        ArrayList<Object> alteracoes = new ArrayList<>();

        if (!original.getDataVencimento().isEqual(dataVencimento)){
            sql.append("DATA_VENCIMENTO = ?, ");
            alteracoes.add(dataVencimento);
        }

        if (original.getFkPlano() != fkPlano){
            sql.append("FK_PLANO_ID = ?, ");
            alteracoes.add(fkPlano);
        }

        if (original.getFkEndereco() != fkEndereco){
            sql.append("FK_ENDERECO_ID = ?, ");
            alteracoes.add(fkEndereco);
        }

        if (!Objects.equals(statusContrato, original.getStatusContrato().getCodigo())){
            sql.append("STATUS_CONTRATO = ?, ");
            alteracoes.add(statusContrato);
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