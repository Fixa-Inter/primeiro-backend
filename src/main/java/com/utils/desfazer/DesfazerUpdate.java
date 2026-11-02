package com.utils.desfazer;

import java.lang.reflect.InvocationTargetException;

// Volta o registro para os dados antes da edição
public class DesfazerUpdate implements ComandoDesfazer {
    private final Class<?> daoClass;
    private final Object modelAntiga;

    // Guarda o DAO e os dados antes da edição
    public DesfazerUpdate(Class<?> daoClass, Object modelAntiga) {
        this.daoClass = daoClass;
        this.modelAntiga = modelAntiga;
    }

    // Reatualiza o registro com os dados antigos
    @Override
    public void desfazer() {
        try (AutoCloseable dao = (AutoCloseable) daoClass.getConstructor().newInstance()) {
            int id = ((Number) modelAntiga.getClass().getMethod("getId").invoke(modelAntiga)).intValue();
            Object modelAtual = daoClass.getMethod("pesquisarPorId", int.class).invoke(dao, id);
            if (modelAtual == null) {
                throw new IllegalStateException("Registro não encontrado para desfazer a atualização.");
            }
            java.lang.reflect.Method atualizar = daoClass.getMethod("atualizar", modelAntiga.getClass(), modelAntiga.getClass());
            atualizar.invoke(dao, modelAtual, modelAntiga);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Não foi possível desfazer a atualização.", e.getCause());
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível desfazer a atualização.", e);
        }
    }

}
