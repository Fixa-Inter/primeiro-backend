package com.DAO;

import com.DTO.EnderecoDTO;
import com.model.Endereco;
import com.model.Filtro;
import com.model.Instituicao;
import com.model.enums.OperacaoFiltro;
import com.model.enums.StatusContrato;
import com.model.enums.TipoInstituicao;
import org.postgresql.core.SqlCommand;
import com.utils.desfazer.DesfazerDelete;
import com.utils.desfazer.DesfazerDeleteFactory;
import com.utils.desfazer.DesfazerInsert;
import com.utils.desfazer.DesfazerUpdate;
import com.utils.desfazer.GerenciadorDesfazer;
import jakarta.servlet.http.HttpServletRequest;

import javax.print.attribute.standard.JobKOctets;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Acesso ao banco para a tabela de endereços
public class EnderecoDAO extends DAO{

    public static final Map<String, String> camposFiltraveis = Map.of(
            "ID", "Id",
            "NOME", "Nome",
            "RUA", "Rua",
            "BAIRRO", "Bairro",
            "CIDADE", "Cidade",
            "ESTADO", "Estado",
            "CEP", "CEP",
            "CNPJ", "CNPJ",
            "FK_INSTITUICAO_ID", "Instituição"
    );

    public static final Map<String, List<OperacaoFiltro>> operacoesPorCampo = Map.of(
            "ID", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "NOME", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
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

    // Converte o valor para o tipo do campo filtrado
    public Object converterValor(String campo, String valor) {
        try {
            return switch (campo) {
                case "ID", "FK_INSTITUICAO_ID" -> Integer.parseInt(valor);
                case "NOME", "RUA","BAIRRO", "CIDADE", "ESTADO", "CEP", "CNPJ" -> valor;
                default -> throw new IllegalArgumentException();
            };
        } catch (DateTimeParseException |
                 IllegalArgumentException |
                 NullPointerException e) {
            return null;
        }
    }

    // Abre a conexão com o banco
    public EnderecoDAO() throws SQLException {
        super();
    }

    // Insere um endereço e devolve o id gerado
    public int cadastrar(Endereco endereco) throws SQLException {
        String nome = endereco.getNome();
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
                INSERT INTO endereco (NOME, RUA, BAIRRO, COMPLEMENTO, CIDADE, ESTADO, NUMERO, CEP, FK_INSTITUICAO_ID, cnpj)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, rua);
            pstmt.setString(3, bairro);
            pstmt.setString(4, complemento);
            pstmt.setString(5, cidade);
            pstmt.setString(6, estado);
            pstmt.setString(7, numero);
            pstmt.setString(8, cep);
            pstmt.setInt(9, fkInstituicao);
            pstmt.setString(10, cnpj);

            int id;
            try (ResultSet rs = pstmt.executeQuery()) { rs.next(); id = rs.getInt("ID"); }

            conn.commit();
            return id;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

    }

    // Insere um endereço e registra a ação para desfazer
    public int cadastrar(Endereco endereco, HttpServletRequest request) throws SQLException {
        int id = cadastrar(endereco);
        GerenciadorDesfazer.registrarComando(request, new DesfazerInsert(getClass(), id));
        return id;
    }

    // Lista endereços, aplicando filtros, ordenação e pesquisa
    public List<Endereco> listar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia, String pesquisa) throws SQLException{

        List<Endereco> resultado =  new ArrayList<Endereco>();

        String sql = "SELECT id, nome, rua, bairro, complemento, cidade, estado, numero, cep, fk_instituicao_id, cnpj, data_criacao FROM endereco";

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

        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)){
            sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);
        } else {
            sql += " ORDER BY ID ASC";
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

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
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

                    resultado.add(new Endereco(id, nome, rua, bairro, complemento, cidade, estado, numero, cep, cnpj, dataCriacao, fkInstituicao));
                }

            }

            // Início da pesquisa
            if (pesquisa != null && !pesquisa.isEmpty()){
                String pesquisaNormalizada = pesquisa.toLowerCase().trim().replace(" ", "");
                resultado.removeIf(endereco -> !endereco.toString()
                        .toLowerCase()
                        .trim()
                        .replace(" ", "")
                        .contains(pesquisaNormalizada));
            }
            // Fim da pesquisa

        }

        conn.commit();
        return resultado;

    }

    // Busca um endereço pelo id
    public Endereco pesquisarPorId(int idEndereco) throws SQLException {

        String sql = "SELECT id, nome, rua, bairro, complemento, cidade, estado, numero, cep, fk_instituicao_id, cnpj, data_criacao FROM endereco WHERE id = ?";

        Endereco e;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, idEndereco);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao procurar por Endereco");
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
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

                e = new Endereco(id, nome, rua, bairro, complemento, cidade, estado, numero, cep, cnpj, dataCriacao, fkInstituicao);
            }
        }

        conn.commit();
        return e;
    }

    // Busca o endereço pelo CNPJ, ou null se ainda não estiver cadastrado

    // Busca o endereço pelo CNPJ, ou null se ainda não estiver cadastrado
    public Endereco pesquisarPorCnpj(String cnpjInsert) throws SQLException{

        String sql = "SELECT id, nome, rua, bairro, complemento, cidade, estado, numero, cep, fk_instituicao_id, cnpj, data_criacao FROM endereco WHERE cnpj = ?";;

        Endereco e;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, cnpjInsert);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    return null;
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
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

                e = new Endereco(id, nome, rua, bairro, complemento, cidade, estado, numero, cep, cnpj, dataCriacao, fkInstituicao);

            }

        }
        conn.commit();
        return e;

    }

    // Atualiza um endereço, só nos campos que mudaram
    public void atualizar(Endereco original, Endereco alterado) throws SQLException {
        Integer id = alterado.getId();
        String nome = alterado.getNome();
        String rua = alterado.getRua();
        String bairro = alterado.getBairro();
        String complemento = alterado.getComplemento();
        String cidade = alterado.getCidade();
        String estado = alterado.getEstado();
        String numero = alterado.getNumero();
        String cep = alterado.getCep();
        String cnpj = alterado.getCnpj();
        // Não atualiza data_criacao
        Integer fkInstituicao = alterado.getFkInstituicao();

        StringBuilder sql = new StringBuilder("UPDATE endereco SET ");
        List<Object> valores = new ArrayList<>();

        if (!Objects.equals(nome, original.getNome())) {
            sql.append("nome = ?, ");
            valores.add(nome);
        }

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
            sql.append("fk_instituicao_id = ?, ");
            valores.add(fkInstituicao);
        }

        // Não atualiza data_criacao

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

    // Atualiza um endereço e registra a ação para desfazer
    public void atualizar(Endereco original, Endereco alterado, HttpServletRequest request) throws SQLException {
        atualizar(original, alterado);
        GerenciadorDesfazer.registrarComando(request, new DesfazerUpdate(getClass(), original));
    }

    // Exclui um endereço pelo id
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

    // Exclui um endereço e registra a ação para desfazer
    public void remover(int id, HttpServletRequest request) throws SQLException {
        DesfazerDelete comando = DesfazerDeleteFactory.criar(getClass(), id);
        remover(id);
        GerenciadorDesfazer.registrarComando(request, comando);
    }

    // Lista endereços com o nome da instituição, aplicando filtros e pesquisa
    public List<EnderecoDTO> listarEnderecoDTO(List<Filtro> filtros, String campoSequencia, String direcaoSequencia, String pesquisa) throws SQLException {
        List<EnderecoDTO> resultado = new ArrayList<>();
        String sql = "SELECT * FROM vw_endereco";
        if (filtros != null && !filtros.isEmpty()) {
            sql += " WHERE ";
            for (int i = 0; i < filtros.size(); i++) {
                Filtro filtro = filtros.get(i);
                String campo = filtro.getCampoFiltravel();
                if (!camposFiltraveis.containsKey(campo)) throw new IllegalArgumentException("Campo inválido: " + campo);
                if (!operacoesPorCampo.get(campo).contains(filtro.getOperacaoFiltro())) {
                    throw new IllegalArgumentException("Operação inválida para o campo: " + campo);
                }
                if (i > 0) sql += " AND ";
                if (filtro.getOperacaoFiltro() == OperacaoFiltro.CONTEM) {
                    sql += "REPLACE(unaccent(CAST(" + campo + " AS TEXT)), ' ', '') "
                            + filtro.getOperacaoFiltro().getOperadorSQL()
                            + " REPLACE(unaccent(?), ' ', '')";
                } else {
                    sql += campo + " " + filtro.getOperacaoFiltro().getOperadorSQL() + " ?";
                }
            }
        }
        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)){
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
                    resultado.add(new EnderecoDTO(rs.getInt("id"), rs.getString("nome"), rs.getString("rua"),
                            rs.getString("bairro"), rs.getString("complemento"), rs.getString("cidade"),
                            rs.getString("estado"), rs.getString("numero"), rs.getString("cep"),
                            rs.getString("instituicao_nome")));
                }
            }
        }
        if (pesquisa != null && !pesquisa.isBlank()) {
            String termo = pesquisa.toLowerCase().trim().replace(" ", "");
            resultado.removeIf(dto -> !dto.toString().toLowerCase().trim().replace(" ", "").contains(termo));
        }
        conn.commit();
        return resultado;
    }

    // Insere um endereço com o id original, para desfazer uma exclusão
    public int cadastrarCompleto(Endereco endereco) throws SQLException {
        int id = endereco.getId();
        String nome = endereco.getNome();
        LocalDateTime dataCriacao = endereco.getDataCriacao();
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
                INSERT INTO endereco (NOME, RUA, BAIRRO, COMPLEMENTO, CIDADE, ESTADO, NUMERO, CEP, FK_INSTITUICAO_ID, CNPJ, ID, DATA_CRIACAO)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, rua);
            pstmt.setString(3, bairro);
            pstmt.setString(4, complemento);
            pstmt.setString(5, cidade);
            pstmt.setString(6, estado);
            pstmt.setString(7, numero);
            pstmt.setString(8, cep);
            pstmt.setInt(9, fkInstituicao);
            pstmt.setString(10, cnpj);
            pstmt.setInt(11, id);
            pstmt.setTimestamp(12, Timestamp.valueOf(dataCriacao));

            pstmt.executeUpdate();

            // Ajusta o serial do banco se o id for maior que o atual
            String sqlAjustarSerial = "SELECT setval(pg_get_serial_sequence('endereco', 'id'), COALESCE(max(id), 1)) FROM endereco";
            PreparedStatement stmtSerial = conn.prepareStatement(sqlAjustarSerial);
            stmtSerial.executeQuery();

            conn.commit();
            return id;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}
