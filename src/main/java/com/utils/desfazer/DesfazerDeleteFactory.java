package com.utils.desfazer;

import com.DAO.*;
import com.model.Filtro;
import com.model.enums.OperacaoFiltro;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

public final class DesfazerDeleteFactory {
    private DesfazerDeleteFactory() {}

    public static DesfazerDelete criar(Class<?> daoClass, int id) throws SQLException {
        DesfazerDelete comando = new DesfazerDelete();
        Object registro = buscarPorId(daoClass, id);
        if (registro == null) {
            throw new SQLException("Registro não encontrado para preparar desfazer a exclusão.");
        }
        adicionarComDependentes(comando, daoClass, registro);
        return comando;
    }

    private static Object buscarPorId(Class<?> daoClass, int id) throws SQLException {
        try (AutoCloseable dao = (AutoCloseable) daoClass.getConstructor().newInstance()) {
            return daoClass.getMethod("pesquisarPorId", int.class).invoke(dao, id);
        } catch (InvocationTargetException e) {
            Throwable causa = e.getCause();
            if (causa instanceof SQLException sqlException) {
                throw sqlException;
            }
            throw new SQLException("Erro ao localizar o registro que será excluído.", causa);
        } catch (Exception e) {
            throw new SQLException("Erro ao localizar o registro que será excluído.", e);
        }
    }

    private static void adicionarComDependentes(DesfazerDelete comando, Class<?> daoClass, Object model)
            throws SQLException {
        comando.adicionarParaRestaurar(daoClass, model);
        int id = idDe(model);

        if (daoClass == InstituicaoDAO.class) {
            for (Object endereco : buscar(EnderecoDAO.class, "FK_INSTITUICAO_ID", id)) {
                adicionarComDependentes(comando, EnderecoDAO.class, endereco);
            }
        } else if (daoClass == EnderecoDAO.class) {
            for (Object usuario : buscar(UsuarioDAO.class, "FK_ENDERECO_ID", id)) {
                adicionarComDependentes(comando, UsuarioDAO.class, usuario);
            }
            for (Object contrato : buscar(ContratoDAO.class, "FK_ENDERECO_ID", id)) {
                adicionarComDependentes(comando, ContratoDAO.class, contrato);
            }
        } else if (daoClass == PlanoDAO.class) {
            for (Object contrato : buscar(ContratoDAO.class, "FK_PLANO_ID", id)) {
                adicionarComDependentes(comando, ContratoDAO.class, contrato);
            }
        } else if (daoClass == ContratoDAO.class) {
            for (Object pagamento : buscar(PagamentoDAO.class, "FK_CONTRATO_ID", id)) {
                adicionarComDependentes(comando, PagamentoDAO.class, pagamento);
            }
        } else if (daoClass == UsuarioDAO.class) {
            for (Object foto : buscar(FotoUsuarioDAO.class, "FK_USUARIO_ID", id)) {
                adicionarComDependentes(comando, FotoUsuarioDAO.class, foto);
            }
        }
    }

    private static List<?> buscar(Class<?> daoClass, String campo, int valor) throws SQLException {
        try (AutoCloseable dao = (AutoCloseable) daoClass.getConstructor().newInstance()) {
            Filtro filtro = new Filtro(campo, valor, OperacaoFiltro.IGUAL);
            Method metodo = localizarBusca(daoClass);
            return (List<?>) metodo.invoke(dao, List.of(filtro), null, null, null);
        } catch (InvocationTargetException e) {
            Throwable causa = e.getCause();
            if (causa instanceof SQLException sqlException) {
                throw sqlException;
            }
            throw new SQLException("Erro ao buscar registros dependentes para desfazer a exclusão.", causa);
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar registros dependentes para desfazer a exclusão.", e);
        }
    }

    private static Method localizarBusca(Class<?> daoClass) throws NoSuchMethodException {
        for (String nome : List.of("buscar", "listar")) {
            try {
                return daoClass.getMethod(nome, List.class, String.class, String.class, String.class);
            } catch (NoSuchMethodException ignored) {
                // Tenta o outro nome usado pelos DAOs do projeto.
            }
        }
        throw new NoSuchMethodException("DAO não possui busca com filtros: " + daoClass.getSimpleName());
    }

    private static int idDe(Object model) throws SQLException {
        try {
            return ((Number) model.getClass().getMethod("getId").invoke(model)).intValue();
        } catch (Exception e) {
            throw new SQLException("Não foi possível obter o ID de um registro dependente.", e);
        }
    }
}
