package com.DAO;

import com.model.Filtro;
import com.model.Instituicao;
import com.model.enums.MetodoPagamento;
import com.model.enums.OperacaoFiltro;
import com.model.enums.TipoInstituicao;
import com.service.HashSenha;
import com.utils.desfazer.DesfazerDeleteFactory;
import com.utils.desfazer.DesfazerInsert;
import com.utils.desfazer.DesfazerUpdate;
import com.utils.desfazer.GerenciadorDesfazer;
import com.utils.desfazer.DesfazerDelete;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InstituicaoDAO extends DAO{

    // map dos campos que sao filtraveis
    public static final Map<String, String> camposFiltraveis = Map.of(
            "ID", "Id",
            "NOME", "Nome",
            "EMAIL_CORPORATIVO", "Email Corporativo",
            "DATA_CADASTRO", "Data Cadastro",
            "TIPO_INSTITUICAO", "Tipo de Instiuicao",
            "DOMINIO_EMAIL", "Dominio Email"

    );

    public static final Map<String, List<OperacaoFiltro>> operacoesPorCampo = Map.of(
            "ID", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "NOME", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "EMAIL_CORPORATIVO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "DATA_CADASTRO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.MAIOR_QUE,
                    OperacaoFiltro.MAIOR_OU_IGUAL,
                    OperacaoFiltro.MENOR_QUE,
                    OperacaoFiltro.MENOR_OU_IGUAL
            ),
            "TIPO_INSTITUICAO", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "DOMINIO_EMAIL", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            )

    );


    // convertendo String recebida do Servlet
    public Object converterValor(String campo, String valor){

        try {
            return switch (campo){
                case "ID" -> Integer.parseInt(valor);
                case "NOME", "EMAIL_CORPORATIVO", "DOMINIO_EMAIL", "SENHA_HASH" -> valor;
                case "DATA_CADASTRO" -> LocalDate.parse(valor);
                case "TIPO_INSTITUICAO" -> {
                    try {
                        yield TipoInstituicao.converterEnum(valor).getCodigo();
                    } catch (IllegalArgumentException e) {
                        yield TipoInstituicao.converterEnum(
                                Integer.parseInt(valor)
                        ).getCodigo();
                    }
                }
                default -> throw new IllegalArgumentException();
            };
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }

    }


    // construtor de DAO
    public InstituicaoDAO() throws SQLException {
        super();
    }

    // insert
    public int cadastrar(Instituicao instituicao) throws SQLException {

        String nome = instituicao.getNome();
        String emailCorporativo = instituicao.getEmailCorporativo();
        String dominioEmail = instituicao.getDominioEmail();
        Integer tipoInstituicao = instituicao.getTipoInstituicao().getCodigo();
        String senhaHash = instituicao.getSenhaHash();

        // tira as coluna id e data_cadastro pois o default do bd já preenche eles automaticamente


        String sql = """
                     INSERT INTO instituicao (NOME, EMAIL_CORPORATIVO, DOMINIO_EMAIL, TIPO_INSTITUICAO, SENHA_HASH)
                     VALUES (?, ?, ?, ?, ?) RETURNING ID
                     """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, nome);
            pstmt.setString(2, emailCorporativo);
            pstmt.setString(3, dominioEmail);
            pstmt.setInt(4, tipoInstituicao);
            pstmt.setString(5, senhaHash);

            int id;
            try (ResultSet rs = pstmt.executeQuery()) { rs.next(); id = rs.getInt("ID"); }

            conn.commit();
            return id;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public int cadastrar(Instituicao instituicao, HttpServletRequest request) throws SQLException {
        int id = cadastrar(instituicao);
        GerenciadorDesfazer.registrarComando(request, new DesfazerInsert(getClass(), id));
        return id;
    }

    public int cadastrarCompleto(Instituicao instituicao) throws SQLException {
        String sql = "INSERT INTO instituicao (id, nome, email_corporativo, data_cadastro, tipo_instituicao, dominio_email, senha_hash) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, instituicao.getId());
            pstmt.setString(2, instituicao.getNome());
            pstmt.setString(3, instituicao.getEmailCorporativo());
            pstmt.setObject(4, instituicao.getDataCadastro());
            pstmt.setObject(5, instituicao.getTipoInstituicao() == null ? null : instituicao.getTipoInstituicao().getCodigo());
            pstmt.setString(6, instituicao.getDominioEmail());
            pstmt.setString(7, instituicao.getSenhaHash());
            pstmt.executeUpdate();
            try (PreparedStatement serial = conn.prepareStatement("SELECT setval(pg_get_serial_sequence('instituicao', 'id'), COALESCE(max(id), 1)) FROM instituicao")) { serial.executeQuery(); }
            conn.commit();
            return instituicao.getId();
        } catch (SQLException e) { conn.rollback(); throw e; }
    }

    // select
    public List<Instituicao> listar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia, String pesquisa) throws SQLException {

        List<Instituicao> resultado = new ArrayList<>();


        String sql = "SELECT id, nome, email_corporativo, data_cadastro, dominio_email, tipo_instituicao, senha_hash FROM instituicao";

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
                    String nome = rs.getString("nome");
                    String emailCorporativo = rs.getString("email_corporativo");
                    Date dataCadastroSQL = rs.getDate("data_cadastro");
                    LocalDate dataCadastro = (dataCadastroSQL == null ? null : dataCadastroSQL.toLocalDate());
                    String dominioEmail = rs.getString("dominio_email");
                    Integer tipoInstituicao = rs.getInt("tipo_instituicao");
                    String senhaHash = rs.getString("senha_hash");


                    resultado.add(new Instituicao(id, nome, emailCorporativo,dataCadastro, TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail, senhaHash));
                }

            }

            if (pesquisa != null && !pesquisa.isEmpty()) {
                String pesquisaNormalizada = pesquisa.toLowerCase().trim().replace(" ", "");
                resultado.removeIf(instituicao -> !instituicao.toString()
                        .toLowerCase().trim().replace(" ", "")
                        .contains(pesquisaNormalizada));
            }

        }

        conn.commit();
        return resultado;
    }

    // select id
    public Instituicao pesquisarPorId(int idInstituicao) throws SQLException{

        String sql = "SELECT id, nome, email_corporativo, data_cadastro, dominio_email, tipo_instituicao, senha_hash FROM instituicao WHERE id = ?";

        Instituicao i;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, idInstituicao);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao procurar instituicao");
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String emailCorporativo = rs.getString("email_corporativo");
                Date dataCadastroSql = rs.getDate("data_cadastro");
                LocalDate dataCadastro = (dataCadastroSql == null ? null : dataCadastroSql.toLocalDate());
                String dominioEmail = rs.getString("dominio_email");
                Integer tipoInstituicao = rs.getInt("tipo_instituicao");
                String senhaHash = rs.getString("senha_hash");

                i = new Instituicao(id, nome, emailCorporativo,dataCadastro, TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail, senhaHash);

            }

        }
        conn.commit();
        return i;
    }

    // select dominio email para o servlet, pois como a tabela dominio email e unique, precisa de um metodo para verificar se já existe um dominio email igual e mostrar
    // uma excecao com mensagem pra isso, e retorna null caso n tenha esse dominio de email registrado, liberando para o registro no sistema
    public Instituicao pesquisarPorEmailCorporativo(String emailCorporativo) throws SQLException{

        String sql = "SELECT id, nome, email_corporativo, data_cadastro, dominio_email, tipo_instituicao, senha_hash FROM instituicao WHERE email_corporativo = ?";

        Instituicao i;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, emailCorporativo);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    return null;
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                Date dataCadastroSql = rs.getDate("data_cadastro");
                LocalDate dataCadastro = (dataCadastroSql == null ? null : dataCadastroSql.toLocalDate());
                String dominioEmail = rs.getString("dominio_email");
                Integer tipoInstituicao = rs.getInt("tipo_instituicao");
                String senhaHash = rs.getString("senha_hash");

                i = new Instituicao(id, nome, emailCorporativo, dataCadastro, TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail, senhaHash);

            }

        }
        conn.commit();
        return i;

    }

    // select dominio email para o servlet, pois como a tabela dominio email e unique, precisa de um metodo para verificar se já existe um dominio email igual e mostrar
    // uma excecao com mensagem pra isso, e retorna null caso n tenha esse dominio de email registrado, liberando para o registro no sistema
    public Instituicao pesquisarPorDominioEmail(String dominioEmailInsert) throws SQLException{

        String sql = "SELECT id, nome, email_corporativo, data_cadastro, dominio_email, tipo_instituicao, senha_hash FROM instituicao WHERE dominio_email = ?";

        Instituicao i;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, dominioEmailInsert);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                   return null;
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String emailCorporativo = rs.getString("email_corporativo");
                Date dataCadastroSql = rs.getDate("data_cadastro");
                LocalDate dataCadastro = (dataCadastroSql == null ? null : dataCadastroSql.toLocalDate());
                String dominioEmail = rs.getString("dominio_email");
                Integer tipoInstituicao = rs.getInt("tipo_instituicao");
                String senhaHash = rs.getString("senha_hash");

                i = new Instituicao(id, nome, emailCorporativo,dataCadastro, TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail, senhaHash);

            }

        }
        conn.commit();
        return i;

    }


    // update
    public void atualizar(Instituicao original, Instituicao alterada) throws SQLException {

        Integer id = alterada.getId();
        String nome = alterada.getNome();
        String emailCorpoartivo = alterada.getEmailCorporativo();
        Integer tipoInstituicao = alterada.getTipoInstituicao().getCodigo();
        String dominioEmail = alterada.getDominioEmail();
        String senhaHash = alterada.getSenhaHash();

        StringBuilder sql = new StringBuilder("UPDATE instituicao SET ");
        List<Object> valores = new ArrayList<>();

        if (!Objects.equals(nome, original.getNome())){
            sql.append("nome = ?, ");
            valores.add(nome);
        }

        if (!Objects.equals(emailCorpoartivo, original.getEmailCorporativo())){
            sql.append("email_corporativo = ?, ");
            valores.add(emailCorpoartivo);
        }

        // tira data_cadastro pois é um campo que não deve ser atualizado

        if (!Objects.equals(dominioEmail, original.getDominioEmail())){
            sql.append("dominio_email = ?, ");
            valores.add(dominioEmail);
        }

        if (!Objects.equals(tipoInstituicao, original.getTipoInstituicao().getCodigo())){
            sql.append("tipo_instituicao = ?, ");
            valores.add(tipoInstituicao);
        }

        if (!Objects.equals(senhaHash, original.getSenhaHash())){
            sql.append("senha_hash = ?, ");
            valores.add(senhaHash);
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

    public void atualizar(Instituicao original, Instituicao alterada, HttpServletRequest request) throws SQLException {
        atualizar(original, alterada);
        GerenciadorDesfazer.registrarComando(request, new DesfazerUpdate(getClass(), original));
    }


    // delete
    public void remover(int id) throws SQLException {

        String sql = "DELETE FROM instituicao WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            conn.commit();
        } catch (SQLException e){
            conn.rollback();
            throw e;
        }

    }

    public void remover(int id, HttpServletRequest request) throws SQLException {
        DesfazerDelete comando = DesfazerDeleteFactory.criar(getClass(), id);
        remover(id);
        GerenciadorDesfazer.registrarComando(request, comando);
    }

}
