package com.DAO;

import com.model.Instituicao;
import org.postgresql.core.SqlCommand;

import javax.print.attribute.standard.JobKOctets;
import java.io.ObjectStreamException;
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

            "id", "ID",
            "nome", "Nome",
            "esta_ativo", "Esta Ativo",
            "email_corporativo", "Email Corporativo",
            "data_cadastro", "Data Cadastro",
            "cnpj", "CNPJ"
    );


    // convertendo String recebida do Servlet
    public Object converterValor(String campo, String valor){

        try {
            return switch (campo){
                case "id" -> Integer.parseInt(valor);
                case "nome", "cnpj", "email_corporativo" -> valor;
                case "esta_ativo" -> Boolean.parseBoolean(valor);
                case "data_cadastro" -> LocalDate.parse(valor);
                default -> throw new IllegalArgumentException();
            };
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null; // nao tem filtro mas funciona normal
        }

    }


    // construtor de DAO
    public InstituicaoDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // insert
    public void cadastrar(Instituicao instituicao) throws SQLException {

        // recebe os dados de instituicao a ser cadastrada

        String nome = instituicao.getNome();
        Boolean estaAtivo = instituicao.getEstaAtivo();
        String emailCorporativo = instituicao.getEmailCorporativo();
        LocalDate dataCadastro = instituicao.getDataCadastro();
        String cnpj = instituicao.getCnpj();

        // string com comando SQL

        String sql = """
                     INSERT INTO instituicao (NOME, ESTA_ATIVO, EMAIL_CORPORATIVO, DATA_CADASTRO, CNPJ)
                     VALUES (?, ?, ?, ?, ?)
                     """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            // colocando valores no script

            pstmt.setString(1, nome);
            pstmt.setBoolean(2, estaAtivo);
            pstmt.setString(3, emailCorporativo);
            pstmt.setObject(4, dataCadastro);
            pstmt.setString(5, cnpj);

            pstmt.execute();

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }

    // select
    public List<Instituicao> listar(String campoFiltro, Object valorFiltro, String campoSequencia, String direcaoSequencia) throws SQLException {

        // variavel auxiliar para verificar se vai ter filtro
        boolean temFiltro = true;

        List<Instituicao> instituicoes = new ArrayList<>();

        String sql = "SELECT id, nome, esta_ativo, email_corporativo, data_cadastro, cnpj FROM instituicao";

        // Verificando se tem o campo de filtragem digitado pelo usuario
        if (campoFiltro != null && camposFiltraveis.containsKey(campoFiltro)){
            sql += " WHERE %s = ?".formatted(campoFiltro);
        } else {
            temFiltro = false;
        }

        // verificando campo e direcao da ordenacao
        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)){
            sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);
        } else {
            sql += "ORDER BY ID ASC";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            // verifica se tem filtro, se sim define a variavel do comando SQL
            if (temFiltro){
                pstmt.setObject(1, valorFiltro);
            }

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    boolean estaAtivo = rs.getBoolean("esta_ativo");
                    String emailCorporativo = rs.getString("email_corporativo");
                    Date dataCadastroSQL = rs.getDate("data_cadastro");
                    LocalDate dataCadastro = (dataCadastroSQL == null ? null : dataCadastroSQL.toLocalDate());
                    String cnpj = rs.getString("cnpj");


                    instituicoes.add(new Instituicao(id, nome, estaAtivo, emailCorporativo,dataCadastro, cnpj));
                }

            }

        }

        conn.commit();
        return instituicoes;
    }

    // select id

    public Instituicao pesquisarIdInstituicao(int idInstituicao) throws SQLException{

        // String com comando SQL

        String sql = "SELECT id, nome, esta_ativo, email_corporativo, data_cadastro, cnpj FROM instituicao WHERE id = ?";

        Instituicao i;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, idInstituicao);

            try (ResultSet rs = pstmt.executeQuery()){

                // se nao tiver instituicao
                if (!rs.next()){
                    throw new SQLException("Erro ao procurar instituicao");
                }

                // recebe informacoes do select e adiciona cada uma a sua variavel

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                boolean esta_ativo = rs.getBoolean("esta_ativo");
                String email_corporativo = rs.getString("email_corporativo");
                Date dataCadastroSql = rs.getDate("data_cadastro");
                LocalDate data_cadastro = (dataCadastroSql == null ? null : dataCadastroSql.toLocalDate());
                String cnpj = rs.getString("cnpj");

                // cria um objeto com essas variaveis

                i = new Instituicao(id, nome, esta_ativo, email_corporativo, data_cadastro, cnpj);

            }

        }
        conn.commit();
        return i;
    }

    // select nome

    public Instituicao pesquisarNomeInstituicao(String nomeInstituicao) throws SQLException{

        // String com comando SQL

        String sql = "SELECT id, nome, esta_ativo, email_corporativo, data_cadastro, cnpj FROM instituicao WHERE nome = ?";

        Instituicao i;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, nomeInstituicao);

            try (ResultSet rs = pstmt.executeQuery()){

                // se nao tiver instituicao
                if (!rs.next()){
                    throw new SQLException("Erro ao procurar insituicao");
                }

                // recebe informacoes do select e adiciona cada uma a sua variavel

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                boolean esta_ativo = rs.getBoolean("esta_ativo");
                String email_corporativo = rs.getString("email_corporativo");
                Date dataCadastroSql = rs.getDate("data_cadastro");
                LocalDate data_cadastro = (dataCadastroSql == null ? null : dataCadastroSql.toLocalDate());
                String cnpj = rs.getString("cnpj");

                // cria um objeto com essas variaveis

                i = new Instituicao(id, nome, esta_ativo, email_corporativo, data_cadastro, cnpj);

            }

        }
        conn.commit();
        return i;

    }


    // update
    public void atualizar(Instituicao original, Instituicao alterada) throws SQLException {

        int id = alterada.getId();
        String nome = alterada.getNome();
        Boolean estaAtivo = alterada.getEstaAtivo();
        String emailCorpoartivo = alterada.getEmailCorporativo();
        LocalDate dataCadastro = alterada.getDataCadastro();
        String cnpj = alterada.getCnpj();

        // String builder para implementar campos do update e Lista para adicionar os valores que vao ser adicionados

        StringBuilder sql = new StringBuilder("UPDATE instituicao SET ");
        List<Object> valores = new ArrayList<>();

        // verifica se os valores novos sao iguais aos antigos, e se nao for altera

        if (!Objects.equals(nome, original.getNome())){
            sql.append("nome = ?, ");
            valores.add(nome);
        }

        if (!Objects.equals(estaAtivo, original.getEstaAtivo())){
            sql.append("esta_ativo = ?, ");
            valores.add(estaAtivo);
        }

        if (!Objects.equals(emailCorpoartivo, original.getEmailCorporativo())){
            sql.append("email_corporativo = ?, ");
            valores.add(emailCorpoartivo);
        }

        if (!Objects.equals(dataCadastro, original.getDataCadastro())){
            sql.append("data_cadastro = ?, ");
            valores.add(dataCadastro);
        }

        if (!Objects.equals(cnpj, original.getCnpj())){
            sql.append("cnpj = ?, ");
            valores.add(cnpj);
        }

        if (valores.isEmpty()){
            return;
        }

        // remocao da ultima virgula + 2 espacos extras no final
        sql.setLength(sql.length() - 2);

        // adiciona WHERE

        sql.append(" WHERE id = ?");
        valores.add(id);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i, valores.get(i));
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