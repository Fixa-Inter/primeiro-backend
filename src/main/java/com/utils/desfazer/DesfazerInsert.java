package com.utils.desfazer;

import java.lang.reflect.InvocationTargetException;

// Remove o registro recém-criado
public class DesfazerInsert implements ComandoDesfazer {
    private final Class<?> daoClass;
    private final int idGerado;

    // Guarda o DAO e o id do registro criado
    public DesfazerInsert(Class<?> daoClass, int idGerado) {
        this.daoClass = daoClass;
        this.idGerado = idGerado;
    }

    // Exclui o registro criado
    @Override
    public void desfazer() {
        try (AutoCloseable dao = (AutoCloseable) daoClass.getConstructor().newInstance()) {
            daoClass.getMethod("remover", int.class).invoke(dao, idGerado);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Não foi possível desfazer o cadastro.", e.getCause());
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível desfazer o cadastro.", e);
        }
    }
}
