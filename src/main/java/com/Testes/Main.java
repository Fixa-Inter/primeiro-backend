package com.Testes;

import com.DAO.EnderecoDAO;
import com.DAO.InstituicaoDAO;
import com.model.Endereco;
import com.model.Instituicao;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    static void main() {

        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // TESTES DAO ENDERECO

        Endereco endereco = new Endereco(null, "Rua zuma de Sá Fernandes", "Presidente Altino", "Ap 103, torre 2", "Osaco", "SP", 323, "06213040", 1);
        Endereco endereco_2 = new Endereco(1, "Rua das Acácias", "Jardim Primavera", "Bloco A", "São Paulo", "SP", 123, "01234-000", 1);
        Endereco endereco_3 = new Endereco(1, "Rua das Palmeiras", "Vila Yara", "Casa 2", "Osasco", "SP", 87, "06020150", 1);

        try (EnderecoDAO dao = new EnderecoDAO()){

            // cadastro
            dao.cadastrar(endereco);
            System.out.print("Endereco cadastrado");

            // select
            Endereco select = dao.pesquisarIdEndereco(2);
            System.out.println("Endereco encontrado: " + "\n" + select);

            // update
            dao.atualizar(endereco_2, endereco_3);
            System.out.println("Endereco trocado!");

            // delete
            dao.remover(7);
            System.out.print("Removido com sucesso!");


        } catch (SQLException erro) {
            System.err.println("Erro ao rodar comando SQL");
            erro.printStackTrace(System.err);
        } catch (ClassNotFoundException erro) {
            System.err.println("Erro ao conectar");;
            erro.printStackTrace(System.err);
        }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


     LocalDate dataCadastroGerminare = LocalDate.of(2026, 01, 10);
    Instituicao germinare = new Instituicao(null, "Germinare", true, "germinare@email_fake", dataCadastroGerminare, "23088915000104");

    try (InstituicaoDAO dao = new InstituicaoDAO()){



    } catch (SQLException e) {
        throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }

    }
}