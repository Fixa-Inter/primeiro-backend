package com.Testes;

import com.DAO.*;
import com.model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    static void main() {

        // -------------------------------------------------------------------------------------------------------------
        // TESTES DAO ENDERECO

//        Endereco endereco = new Endereco(null, "Rua zuma de Sá Fernandes", "Presidente Altino", "Ap 103, torre 2", "Osaco", "SP", 323, "06213040", 1);
//        Endereco endereco_2 = new Endereco(1, "Rua das Acácias", "Jardim Primavera", "Bloco A", "São Paulo", "SP", 123, "01234-000", 1);
//        Endereco endereco_3 = new Endereco(1, "Rua das Palmeiras", "Vila Yara", "Casa 2", "Osasco", "SP", 87, "06020150", 1);
//
//        try (EnderecoDAO dao = new EnderecoDAO()){
//
//            // cadastro
//            dao.cadastrar(endereco);
//            System.out.print("Endereco cadastrado \n");
//
//            // select
//            Endereco select = dao.pesquisarId(2);
//            System.out.println("Endereco encontrado: " + "\n" + select + "\n");
//
//            // update
//            dao.atualizar(endereco_2, endereco_3);
//            System.out.println("Endereco trocado! \n");
//
//            // delete
//            dao.remover(7);
//            System.out.print("Removido com sucesso! \n");
//
//
//        } catch (SQLException erro) {
//            System.err.println("Erro ao rodar comando SQL");
//            erro.printStackTrace(System.err);
//        } catch (ClassNotFoundException erro) {
//            System.err.println("Erro ao conectar");;
//            erro.printStackTrace(System.err);
//        }

    // -----------------------------------------------------------------------------------------------------------------
    // TESTES DAO INSTITUICAO


//    try (InstituicaoDAO dao = new InstituicaoDAO()){

//        Instituicao germinare = dao.pesquisarId(1);
//
//        LocalDate dataCadastroSTAR = LocalDate.of(2026, 02, 23);
//        Instituicao star = new Instituicao(germinare.getId(), "StarMax()", true, "starMAX@email_fake", dataCadastroSTAR, "33011915028614");

//        // cadastro
//        dao.cadastrar(germinare);
//        System.out.println("Instituição cadastrada com sucesso!");

//        // update
//
//        dao.atualizar(germinare, star);
//        System.out.println("Registro atualizado com sucesso!");
//
//
//        Instituicao selectNOME = dao.pesquisarNome("Colégio Técnico Nova Esperança");
//
//        System.out.print(selectNOME);


//    LocalDate dataCadastroBRITO = LocalDate.of(2025, 02, 23);
//    Instituicao brito = new Instituicao(null, "BRITO", true, "BRITO@email_fake", dataCadastroBRITO, "43011915028614");

//    dao.remover(13);
//
//    // select
//    String campoFiltro = "esta_ativo";
//    Object valorFiltro = dao.converterValor(campoFiltro, "true");
//    String campoSequencia = "id";
//    String direcaoSequencia = "ASC";
//
//    List<Instituicao> resultado = dao.listar(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
//
//    System.out.println("Resultado escontrados" + "\n");
//
//    for (int i = 0; i < resultado.size(); i++) {
//        Instituicao item = resultado.get(i);
//        System.out.print("Item " + (i + 1) + ": \n" + item + "\n");
//    }
//
//
//    } catch (SQLException e) {
//        throw new RuntimeException(e);
//    } catch (ClassNotFoundException e) {
//        throw new RuntimeException(e);
//    }

    // -----------------------------------------------------------------------------------------------------------------
    // TESTE USUARIO DAO

//    try (UsuarioDAO dao = new UsuarioDAO()){
//
//        LocalDate dataCadastroMIGUEL = LocalDate.of(2026, 1, 1);
//        LocalDate dataCadastroUltimoAcessoMIGUEL = LocalDate.of(2026, 7, 31);
//
//        LocalDate dataCadastroYUJI = LocalDate.of(2026, 2, 28);
//        LocalDate dataCadastroUltimoAcessoYUJI = LocalDate.of(2026, 8, 11);
//
//        Usuario miguel = new Usuario(null, "Miguel", "admin123", true, "miguel@email_falso", dataCadastroMIGUEL, "Estudante", "Solicitante", 1, dataCadastroUltimoAcessoMIGUEL);


        // insert
//        dao.cadastrar(miguel);
//        System.out.println("Usuario cadastrado com sucesso!");

        // select id
//        System.out.print(dao.pesquisarPorId(11));
//        System.out.println();

        // select pelo nome
//        System.out.println(dao.pesquisarPorNome("Miguel"));
//        System.out.println();

        // update
//        Usuario original = dao.pesquisarPorId(11);
//        Usuario yuji = new Usuario(original.getId(), "Yuji", "admin123", true, "yuji@email_falso", dataCadastroYUJI, "Estudante", "Solicitante", 1, dataCadastroUltimoAcessoYUJI);
//        dao.atualizar(original, yuji);

//        // delete
//        dao.remover(11);
//
//
//        // select
//        String campoFiltro = "esta_ativo";
//        Object valorFiltro = dao.converterValor(campoFiltro, "true");
//        String campoSequencia = "id";
//        String direcaoSequencia = "ASC";
//
//        List<Usuario> resultado = dao.listar(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
//
//        System.out.println("Resultado escontrados" + "\n");
//
//        for (int i = 0; i < resultado.size(); i++) {
//            Usuario item = resultado.get(i);
//            System.out.print("Item " + (i + 1) + ": \n" + item + "\n");
//        }
//
//
//
//    } catch (SQLException e) {
//        throw new RuntimeException(e);
//    } catch (ClassNotFoundException e) {
//        throw new RuntimeException(e);
//    }


    // -----------------------------------------------------------------------------------------------------------------
    // TESTE PAGAMENTO

//    try (PagamentoDAO dao = new PagamentoDAO()){

        LocalDate dataPagamento = LocalDate.of(2026, 1, 1);
//        Pagamento pagamento = new Pagamento(null, (float) 100.0, dataPagamento.atStartOfDay(), true, 1, 1);

//        dao.cadastrar(pagamento);
//        System.out.println("Pagamento inserido com sucesso!");

//        String campoFiltro = "foi_realizado";
//        Object valorFiltro = dao.converterValor(campoFiltro, "true");
//        String campoSequencia = "id";
//        String direcaoSequencia = "ASC";
//
//        List<Pagamento> pagamentos = dao.listar(campoFiltro, valorFiltro, campoSequencia, direcaoSequencia);
//
//        for (int i = 0; i <pagamentos.size(); i++) {
//            Pagamento pag = pagamentos.get(i);
//            System.out.print("Pagamento " + (i + 1) + " :" + "\n" + pag + "\n");
//        }
//
//        Pagamento original = dao.pesquisarId(5);
//        Pagamento alterado = new Pagamento(original.getId(), (float) 150.0, dataPagamento.atStartOfDay(), true, 1, 1);
//
//        dao.atualizar(original, alterado);
//
//        System.out.print(dao.pesquisarId(5));
//
//        dao.remover(5);
//
//    } catch (SQLException e) {
//        throw new RuntimeException(e);
//    } catch (ClassNotFoundException e) {
//        throw new RuntimeException(e);
//    }

    // -----------------------------------------------------------------------------------------------------------------
    // TESTE FOTO USUARIO

        try (FotoUsuarioDAO dao = new FotoUsuarioDAO()){

            // insert

//            LocalDate dataRegistro = LocalDate.of(2026, 1, 1);

//            FotoUsuario fotoUsuario = new FotoUsuario(null, dataRegistro, "url_falsa", 11);
//
//            dao.cadastrar(fotoUsuario);

            // update

//            FotoUsuario original = dao.pesquisarFkID(11);
//            FotoUsuario alterado = new FotoUsuario(original.getId(), dataRegistro, "url falsa nova", 11);
//
//            dao.atualizar(original, alterado);

            // select

//            System.out.print(dao.pesquisarFkID(11));

            // delete

            dao.remover(11);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}