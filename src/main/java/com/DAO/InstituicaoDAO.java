package com.DAO;

import com.model.Filtro;
import com.model.Instituicao;
import com.model.enums.OperacaoFiltro;
import com.model.enums.TipoInstituicao;

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
            "ID", "ID",
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
                case "id" -> Integer.parseInt(valor);
                case "nome", "email_corporativo", "dominio_email" -> valor;
                case "data_cadastro" -> LocalDate.parse(valor);
                case "tipo_instituicao" -> TipoInstituicao.converterEnum(valor);
                default -> throw new IllegalArgumentException();
            };
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }

    }


    // construtor de DAO
    public InstituicaoDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // insert
    public void cadastrar(Instituicao instituicao) throws SQLException {

        String nome = instituicao.getNome();
        String emailCorporativo = instituicao.getEmailCorporativo();
        String dominioEmail = instituicao.getDominioEmail();
        Integer tipoInstituicao = instituicao.getTipoDeInstituicao().getCodigo();

        // tira as coluna id e data_cadastro pois o default do bd já preenche eles automaticamente


        String sql = """
                     INSERT INTO instituicao (NOME, EMAIL_CORPORATIVO, DOMINIO_EMAIL, TIPO_INSTITUICAO)
                     VALUES (?, ?, ?, ?)
                     """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, nome);
            pstmt.setString(2, emailCorporativo);
            pstmt.setString(3, dominioEmail);
            pstmt.setInt(4, tipoInstituicao);

            pstmt.execute();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // select
    public List<Instituicao> listar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia) throws SQLException {

        List<Instituicao> resultado = new ArrayList<>();


        String sql = "SELECT id, nome, email_corporativo, data_cadastro, dominio_email, tipo_instituicao FROM instituicao";

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


                    resultado.add(new Instituicao(id, nome, emailCorporativo,dataCadastro, TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail));
                }

            }

        }

        conn.commit();
        return resultado;
    }

    // select id
    public Instituicao pesquisarId(int idInstituicao) throws SQLException{

        String sql = "SELECT id, nome, email_corporativo, data_cadastro, dominio_email, tipo_instituicao FROM instituicao WHERE id = ?";

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

                i = new Instituicao(id, nome, emailCorporativo,dataCadastro, TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail);

            }

        }
        conn.commit();
        return i;
    }

    // select nome
    public Instituicao pesquisarNome(String nomeInstituicao) throws SQLException{

        String sql = "SELECT id, nome, email_corporativo, data_cadastro, dominio_email, tipo_instituicao FROM instituicao WHERE nome = ?";

        Instituicao i;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, nomeInstituicao);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao procurar insituicao");
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String emailCorporativo = rs.getString("email_corporativo");
                Date dataCadastroSql = rs.getDate("data_cadastro");
                LocalDate dataCadastro = (dataCadastroSql == null ? null : dataCadastroSql.toLocalDate());
                String dominioEmail = rs.getString("dominio_email");
                Integer tipoInstituicao = rs.getInt("tipo_instituicao");

                i = new Instituicao(id, nome, emailCorporativo,dataCadastro, TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail);

            }

        }
        conn.commit();
        return i;

    }

    // select dominio email para o servlet, pois como a tabela dominio email e unique, precisa de um metodo para verificar se já existe um dominio email igual e mostrar
    // uma excecao com mensagem pra isso, e retorna null caso n tenha esse dominio de email registrado, liberando para o registro no sistema

    public Instituicao pesquisarDominioEmail(String dominioEmailInsert) throws SQLException{

        String sql = "SELECT id, nome, email_corporativo, data_cadastro, dominio_email, tipo_instituicao FROM instituicao WHERE dominio_email = ?";

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

                i = new Instituicao(id, nome, emailCorporativo,dataCadastro, TipoInstituicao.converterEnum(tipoInstituicao), dominioEmail);

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
        Integer tipoInstituicao = alterada.getTipoDeInstituicao().getCodigo();
        String dominioEmail = alterada.getDominioEmail();

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

        if (!Objects.equals(tipoInstituicao, original.getTipoDeInstituicao().getCodigo())){
            sql.append("tipo_instituicao = ?, ");
            valores.add(tipoInstituicao);
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
}