package com.DAO;

import com.model.Endereco;
import org.postgresql.core.SqlCommand;

import javax.print.attribute.standard.JobKOctets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnderecoDAO extends DAO{

    // construtor de DAO
    public EnderecoDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // insert
    public void cadastrar(Endereco endereco) throws SQLException {

        // recebe dados do endereco a ser cadastrado

        String rua = endereco.getRua();
        String bairro = endereco.getBairro();
        String complemento = endereco.getComplemento();
        String cidade = endereco.getCidade();
        String estado = endereco.getEstado();
        Integer numero = endereco.getNumero();
        String cep = endereco.getCep();
        Integer fkInstituicao = endereco.getFkInstituicao();

        // deixa complemento como null se estiver vazio

        if (complemento == null || complemento.isBlank()){
            complemento = null;
        }

        // string com comando sql

        String sql = """
                INSERT INTO endereco (RUA, BAIRRO, COMPLEMENTO, CIDADE, ESTADO, NUMERO, CEP, FK_INSTITUICAO_ID)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // colocando valores no script sql

            pstmt.setString(1, rua);
            pstmt.setString(2, bairro);
            pstmt.setString(3, complemento);
            pstmt.setString(4, cidade);
            pstmt.setString(5, estado);
            pstmt.setInt(6, numero);
            pstmt.setString(7, cep);
            pstmt.setInt(8, fkInstituicao);

            pstmt.execute();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }

    }

    // select
    public Endereco pesquisarIdEndereco(int idInstituicao) throws SQLException {

        // string com comando sql

        String sql = "SELECT id, rua, bairro, complemento, cidade, estado, numero, cep, fk_instituicao_id FROM endereco WHERE fk_instituicao_id = ?";

        Endereco e;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, idInstituicao);

            try (ResultSet rs = pstmt.executeQuery()){

                // se nao tiver o endereco ele manda essa excecao
                if (!rs.next()){
                    throw new SQLException("Erro ao procurar por Endereco");
                }

                // recebe informacoes do select e adiciona cada uma a sua variavel

                int id = rs.getInt("id");
                String rua = rs.getString("rua");
                String bairro = rs.getString("bairro");
                String complemento = rs.getString("complemento");
                String cidade = rs.getString("cidade");
                String estado = rs.getString("estado");
                int numero = rs.getInt("numero");
                String cep = rs.getString("cep");
                int fkInstituicao = rs.getInt("fk_instituicao_id"); // duvida: aqui eu coloco o nome do que esta no BD ou na model??

                // cria um objeto endereco com essas variaveis

                e = new Endereco(id, rua, bairro, complemento, cidade, estado, numero, cep, fkInstituicao);
            }
        }

        conn.commit();
        return e;
    }

    // update
    public void atualizar(Endereco original, Endereco alterado) throws SQLException {


        // recebe dados do endereco alterado

        Integer id = alterado.getId();
        String rua = alterado.getRua();
        String bairro = alterado.getBairro();
        String complemento = alterado.getComplemento();
        String cidade = alterado.getCidade();
        String estado = alterado.getEstado();
        Integer numero = alterado.getNumero();
        String cep = alterado.getCep();
        Integer fkInstituicao = alterado.getFkInstituicao();

        // String builder para implementar campos do update e Lista para adicionar os valores que vao ser adicionados

        StringBuilder sql = new StringBuilder("UPDATE endereco SET ");
        List<Object> valores = new ArrayList<>();

        // verifica se os valores novos sao iguais aos antigos, e se nao for altera

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

        if (valores.isEmpty()){
            return;
        }

        // remocao da ultima virgula + 2 espacos extras no final
        sql.setLength(sql.length() - 2);

        // adiciona a clausula where
        sql.append(" WHERE id = ?");
        valores.add(id);

        // seta os valores adicionados
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

        // string com comando SQL

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