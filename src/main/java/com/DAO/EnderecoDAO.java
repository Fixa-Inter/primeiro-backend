package com.DAO;

import com.model.Endereco;

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

        Integer id = endereco.getId();
        String rua = endereco.getRua();
        String bairro = endereco.getBairro();
        String complemento = endereco.getComplemento();
        String cidade = endereco.getCidade();
        String estado = endereco.getEstado();
        Integer numero = endereco.getNumero();
        String cep = endereco.getCep();
        Integer fkInstituicao = endereco.getFkInstituicao();

        if (complemento == null || complemento.isBlank()){
            complemento = null;
        }

        String sql = """
                INSERT INTO ENDERECO (ID, RUA, BAIRRO, COMPLEMENTO, CIDADE, ESTADO, NUMERO, CEP, FK_INSTITUICAO_ID)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, rua);
            pstmt.setString(3, bairro);
            pstmt.setString(4, complemento);
            pstmt.setString(5, cidade);
            pstmt.setString(6, estado);
            pstmt.setInt(7, numero);
            pstmt.setString(8, cep);
            pstmt.setInt(9, fkInstituicao);

            pstmt.execute();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }

    }
}