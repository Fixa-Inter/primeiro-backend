package com.DAO;

import com.model.Instituicao;
import org.postgresql.core.SqlCommand;

import javax.print.attribute.standard.JobKOctets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InstituicaoDAO extends DAO{

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
}
