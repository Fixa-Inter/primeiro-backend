package com.utils.desfazer;

// Ação que sabe reverter a última alteração
public interface ComandoDesfazer {
    // Reverte a última alteração
    public void desfazer();
}
