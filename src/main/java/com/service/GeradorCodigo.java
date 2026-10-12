package com.service;

import java.security.SecureRandom; // random é previsivel, esse com secure não

public class GeradorCodigo {

    private static final SecureRandom random = new SecureRandom();

    public String gerar() {
        int numero = random.nextInt(1000000); // o a 999999
        return String.format("%06d", numero); // formata transformando em String
    }

}
