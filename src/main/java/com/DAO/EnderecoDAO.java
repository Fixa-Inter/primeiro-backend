package com.DAO;

import com.model.Endereco;
import com.model.Filtro;
import com.model.Instituicao;
import com.model.enums.OperacaoFiltro;
import com.model.enums.TipoInstituicao;
import org.postgresql.core.SqlCommand;

import javax.print.attribute.standard.JobKOctets;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EnderecoDAO extends DAO{

    public static final Map<String, String> camposFiltraveis = Map.of(
            "RUA", "Rua",
            "BAIRRO", "Bairro",
            "CIDADE", "Cidade",
            "ESTADO", "Estado",
            "CEP", "CEP",
            "CNPJ", "CNPJ",
            "FK_INSTITUICAO_ID", "Instituição"
    );

    public static final Map<String, List<OperacaoFiltro>> operacoesPorCampo = Map.of(
            "RUA", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "BAIRRO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "CIDADE", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "ESTADO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "CEP", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "CNPJ", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "FK_INSTITUICAO_ID", List.of(
                    OperacaoFiltro.IGUAL
            )
    );


    // construtor de DAO
    public EnderecoDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // insert
    public void cadastrar(Endereco endereco) throws SQLException {

        String rua = endereco.getRua();
        String bairro = endereco.getBairro();
        String complemento = endereco.getComplemento();
        String cidade = endereco.getCidade();
        String estado = endereco.getEstado();
        String numero = endereco.getNumero();
        String cep = endereco.getCep();
        Integer fkInstituicao = endereco.getFkInstituicao();
        String cnpj = endereco.getCnpj();


        if (complemento == null || complemento.isBlank()){
            complemento = null;
        }


        String sql = """
                INSERT INTO endereco (RUA, BAIRRO, COMPLEMENTO, CIDADE, ESTADO, NUMERO, CEP, FK_INSTITUICAO_ID, cnpj)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rua);
            pstmt.setString(2, bairro);
            pstmt.setString(3, complemento);
            pstmt.setString(4, cidade);
            pstmt.setString(5, estado);
            pstmt.setString(6, numero);
            pstmt.setString(7, cep);
            pstmt.setInt(8, fkInstituicao);
            pstmt.setString(9, cnpj);

            pstmt.execute();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

    }

    // select
    public List<Endereco> listar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia) throws SQLException{

        List<Endereco> resultado =  new ArrayList<Endereco>();

        String sql = "SELECT id, rua, bairro, complemento, cidade, estado, numero, cep, fk_instituicao_id, cnpj, data_criacao FROM endereco";

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
                    String rua = rs.getString("rua");
                    String bairro = rs.getString("bairro");
                    String complemento = rs.getString("complemento");
                    String cidade = rs.getString("cidade");
                    String estado = rs.getString("estado");
                    String numero = rs.getString("numero");
                    String cep = rs.getString("cep");
                    Integer fkInstituicao = rs.getInt("fk_instituicao_id");
                    String cnpj = rs.getString("cnpj");

                    Timestamp dataCriacaoSQL = rs.getTimestamp("data_criacao");
                    LocalDateTime dataCriacao = (dataCriacaoSQL == null
                            ? null
                            : dataCriacaoSQL.toLocalDateTime());


                    resultado.add(new Endereco(id, rua, bairro, complemento, cidade, estado, numero, cep, cnpj, dataCriacao, fkInstituicao));                }

            }

        }

        conn.commit();
        return resultado;

    }


    public Endereco pesquisarId(int idInstituicao) throws SQLException {


        String sql = "SELECT id, rua, bairro, complemento, cidade, estado, numero, cep, fk_instituicao_id, cnpj, data_criacao FROM endereco WHERE fk_instituicao_id = ?";

        Endereco e;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, idInstituicao);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao procurar por Endereco");
                }

                int id = rs.getInt("id");
                String rua = rs.getString("rua");
                String bairro = rs.getString("bairro");
                String complemento = rs.getString("complemento");
                String cidade = rs.getString("cidade");
                String estado = rs.getString("estado");
                String numero = rs.getString("numero");
                String cep = rs.getString("cep");
                String cnpj = rs.getString("Cnpj");
                LocalDateTime dataCriacao = rs.getTimestamp("data_criacao").toLocalDateTime();
                int fkInstituicao = rs.getInt("fk_instituicao_id");

                e = new Endereco(id, rua, bairro, complemento, cidade, estado, numero, cep, cnpj, dataCriacao, fkInstituicao);
            }
        }

        conn.commit();
        return e;
    }

    // select cnpj para o servlet, pois como a tabela cnpj e unique, precisa de um metodo para verificar se já existe um cnpj igual e mostrar
    // uma excecao com mensagem pra isso, e retorna null caso n tenha esse cnpj registrado, liberando para o registro no sistema

    public Endereco pesquisarcnpj(String cnpjInsert) throws SQLException{

        String sql = "SELECT id, rua, bairro, complemento, cidade, estado, numero, cep, fk_instituicao_id, cnpj, data_criacao FROM endereco WHERE cnpj = ?";;

        Endereco e;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, cnpjInsert);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    return null;
                }

                int id = rs.getInt("id");
                String rua = rs.getString("rua");
                String bairro = rs.getString("bairro");
                String complemento = rs.getString("complemento");
                String cidade = rs.getString("cidade");
                String estado = rs.getString("estado");
                String numero = rs.getString("numero");
                String cep = rs.getString("cep");
                String cnpj = rs.getString("Cnpj");
                LocalDateTime dataCriacao = rs.getTimestamp("data_criacao").toLocalDateTime();
                int fkInstituicao = rs.getInt("fk_instituicao_id");

                e = new Endereco(id, rua, bairro, complemento, cidade, estado, numero, cep, cnpj, dataCriacao, fkInstituicao);

            }

        }
        conn.commit();
        return e;

    }

    // update
    public void atualizar(Endereco original, Endereco alterado) throws SQLException {


        Integer id = alterado.getId();
        String rua = alterado.getRua();
        String bairro = alterado.getBairro();
        String complemento = alterado.getComplemento();
        String cidade = alterado.getCidade();
        String estado = alterado.getEstado();
        String numero = alterado.getNumero();
        String cep = alterado.getCep();
        String cnpj = alterado.getCnpj();
        // ignora data_cricao pois e um valor que nao deve ser atualizado
        Integer fkInstituicao = alterado.getFkInstituicao();


        StringBuilder sql = new StringBuilder("UPDATE endereco SET ");
        List<Object> valores = new ArrayList<>();


        if(!Objects.equals(rua, original.getRua())){
            sql.append("rua = ?, ");
            valores.add(rua);
        }

        if(!Objects.equals(bairro, original.getBairro())){
            sql.append("bairro = ?, ");
            valores.add(bairro);
        }

        if(!Objects.equals(complemento, original.getComplemento())){
            sql.append("complemento = ?, ");
            valores.add(complemento);
        }

        if(!Objects.equals(cidade, original.getCidade())){
            sql.append("cidade = ?, ");
            valores.add(cidade);
        }

        if(!Objects.equals(estado, original.getEstado())){
            sql.append("estado = ?, ");
            valores.add(estado);
        }

        if(!Objects.equals(numero, original.getNumero())){
            sql.append("numero = ?, ");
            valores.add(numero);
        }

        if(!Objects.equals(cep, original.getCep())){
            sql.append("cep = ?, ");
            valores.add(cep);
        }

        if(!Objects.equals(fkInstituicao, original.getFkInstituicao())){
            sql.append("fkInstituicao = ?, ");
            valores.add(fkInstituicao);
        }

        // nao tem dataCriacao pois é um campo que não deve ser atualizado

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

        String sql = "DELETE FROM endereco WHERE ID = ?";

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)){
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e){
            conn.rollback();
            throw e;
        }
    }
}