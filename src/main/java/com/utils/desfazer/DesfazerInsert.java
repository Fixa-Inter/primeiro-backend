package com.utils.desfazer;

import java.lang.reflect.InvocationTargetException;

public class DesfazerInsert implements ComandoDesfazer {
    private final Class<?> daoClass;
    private final int idGerado;

    public DesfazerInsert(Class<?> daoClass, int idGerado) {
        this.daoClass = daoClass;
        this.idGerado = idGerado;
    }

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
