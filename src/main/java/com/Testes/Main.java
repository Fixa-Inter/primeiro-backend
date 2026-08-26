package com.Testes;

import com.DAO.EnderecoDAO;
import com.DAO.MetodoPagamentoDAO;
import com.DAO.PlanoDAO;
import com.DAO.SuperAdministradorDAO;
import com.model.Endereco;
import com.model.MetodoPagamento;
import com.model.Plano;
import com.model.SuperAdministrador;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    static void main() {

        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // TESTES DAO ENDERECO

//        Endereco endereco = new Endereco(null, "Rua zuma de Sá Fernandes", "Presidente Altino", "Ap 103, torre 2", "Osaco", "SP", 323, "06213040", 1);
//        Endereco endereco_2 = new Endereco(1, "Rua das Acácias", "Jardim Primavera", "Bloco A", "São Paulo", "SP", 123, "01234-000", 1);
//        Endereco endereco_3 = new Endereco(1, "Rua das Palmeiras", "Vila Yara", "Casa 2", "Osasco", "SP", 87, "06020150", 1);
//
//        try (EnderecoDAO dao = new EnderecoDAO()){
//
//            // cadastro
//            dao.cadastrar(endereco);
//            System.out.print("Endereco cadastrado");
//
//            // select
//            Endereco select = dao.pesquisarIdInstituicao(2);
//            System.out.println("Endereco encontrado: " + "\n" + select);
//
//            // update
//            dao.atualizar(endereco_2, endereco_3);
//            System.out.println("Endereco trocado!");
//
//            // delete
//            dao.remover(7);
//            System.out.print("Removido com sucesso!");
//
//
//        } catch (SQLException erro) {
//            System.err.println("Erro ao rodar comando SQL");
//            erro.printStackTrace(System.err);
//        } catch (ClassNotFoundException erro) {
//            System.err.println("Erro ao conectar");;
//            erro.printStackTrace(System.err);
//        }

        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //TESTES DAO METODO_PAGAMENTO
//        MetodoPagamento m1 = new MetodoPagamento(null, "Cheque");
//        MetodoPagamento m2 = new MetodoPagamento(null,"Boleto");
//
//        try(MetodoPagamentoDAO daoMetodoPagamento = new MetodoPagamentoDAO()){
        //inserir
//            daoMetodoPagamento.cadastrar(m1);
//
//            //select
//            for (int i = 0; i < daoMetodoPagamento.buscar().size(); i++) {
//                MetodoPagamento metodoPagtoExibir;
//
//                metodoPagtoExibir = new MetodoPagamento(daoMetodoPagamento.buscar().get(i).getId(),daoMetodoPagamento.buscar().get(i).getDescricao());
//                System.out.println("%d  | %s".formatted(metodoPagtoExibir.getId(),metodoPagtoExibir.getDescricao()));
//            }
//
//            //alterar
//            daoMetodoPagamento.atualizar(daoMetodoPagamento.pesquisarPorId(5),m2);
//            //select
//            for (int i = 0; i < daoMetodoPagamento.buscar().size(); i++) {
//                MetodoPagamento metodoPagtoExibir;
//
//                metodoPagtoExibir = new MetodoPagamento(daoMetodoPagamento.buscar().get(i).getId(),daoMetodoPagamento.buscar().get(i).getDescricao());
//                System.out.println("%d  | %s".formatted(metodoPagtoExibir.getId(),metodoPagtoExibir.getDescricao()));
//            }
//
//            //delete
//            daoMetodoPagamento.remover(13);
//
//
//            //select
//            for (int i = 0; i < daoMetodoPagamento.buscar().size(); i++) {
//                MetodoPagamento metodoPagtoExibir;
//
//                metodoPagtoExibir = new MetodoPagamento(daoMetodoPagamento.buscar().get(i).getId(),daoMetodoPagamento.buscar().get(i).getDescricao());
//                System.out.println("%d  | %s".formatted(metodoPagtoExibir.getId(),metodoPagtoExibir.getDescricao()));
//            }
//        } catch (SQLException e) {
//            System.out.print(e.getMessage());
//            e.printStackTrace(System.err); //q isso?
//        } catch (ClassNotFoundException e) {
//            System.out.print(e.getMessage());
//            e.printStackTrace(System.err); //q isso?
//        }
//
//        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//        //TESTE DAO PLANO
//        Plano p1 = new Plano(
//                null,
//                "TESTE",
//                500.00,
//                12,
//                "Plano de teste anual"
//        );
//
//        Plano p2 = new Plano(
//                null,
//                "TESTE",
//                1000.00,
//                12,
//                "Plano de teste anual"
//        );
//
//        try (PlanoDAO pdao = new PlanoDAO()) {

//            // INSERT
//            pdao.cadastrar(p1);
//
//            Integer id = p1.getId();
//
//            // SELECT POR ID
//            Plano original = pdao.pesquisarPorId(8);
//
//            System.out.println(
//                    "%d | %s | %.2f | %d | %s".formatted(
//                            original.getId(),
//                            original.getNome(),
//                            original.getValorMensal(),
//                            original.getDuracaoMeses(),
//                            original.getDescricao()
//                    )
//            );
//
//            // ALTERAR
//            pdao.atualizar(original, p2);
//
//            // SELECT DEPOIS DA ALTERAÇÃO
//            Plano atualizado = pdao.pesquisarPorId(8);
//
//            System.out.println(
//                    "%d | %s | %.2f | %d | %s".formatted(
//                            atualizado.getId(),
//                            atualizado.getNome(),
//                            atualizado.getValorMensal(),
//                            atualizado.getDuracaoMeses(),
//                            atualizado.getDescricao()
//                    )

            //delete

//            pdao.remover(17);
//
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //TESTE SUPER ADMINISTRADOR
//        try {
//            SuperAdministradorDAO sadao = new SuperAdministradorDAO();
//
//            sadao.remover(2);
//
//            ArrayList<SuperAdministrador> select = sadao.buscar(null,null,null,null);
//
//            for (SuperAdministrador sadmin : select){
//                System.out.println("%d | %s | %s | %s".formatted(sadmin.getId(), sadmin.getNome(), sadmin.getFuncao(), sadmin.getEmail()));
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

        // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    }
}