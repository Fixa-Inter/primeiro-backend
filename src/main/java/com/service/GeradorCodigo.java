package com.service;

import java.security.SecureRandom;  // Gerador seguro, não previsível

// Gera o código de verificação do cadastro
public class GeradorCodigo {

    private static final SecureRandom random = new SecureRandom();

    // Gera um código numérico de 6 dígitos
    public String gerar() {
        int numero = random.nextInt(1000000);  // De 0 a 999999
        return String.format("%06d", numero);  // Completa com zeros até 6 dígitos
    }

}
