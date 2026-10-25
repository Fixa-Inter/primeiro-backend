package com.utils.desfazer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DesfazerDelete implements ComandoDesfazer {
    private final List<Registro> registros = new ArrayList<>();

    public void adicionarParaRestaurar(Class<?> daoClass, Object model) {
        registros.add(new Registro(daoClass, model));
    }

    @Override
    public void desfazer() {
        for (Registro registro : registros) {
            try (AutoCloseable dao = (AutoCloseable) registro.daoClass.getConstructor().newInstance()) {
                registro.daoClass.getMethod("cadastrarCompleto", registro.model.getClass()).invoke(dao, registro.model);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Não foi possível restaurar um registro excluído.", e.getCause());
            } catch (Exception e) {
                throw new RuntimeException("Não foi possível restaurar um registro excluído.", e);
            }
        }
    }

    private static class Registro {
        private final Class<?> daoClass;
        private final Object model;

        private Registro(Class<?> daoClass, Object model) {
            this.daoClass = daoClass;
            this.model = model;
        }
    }
}
