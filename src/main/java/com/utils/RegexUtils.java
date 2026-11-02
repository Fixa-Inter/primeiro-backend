package com.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Validações de formato por regex
public class RegexUtils {
    public static final String REGEX_EMAIL = "[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+";
    public static final String REGEX_CNPJ = "^[A-Z0-9]{2}\\.?[A-Z0-9]{3}\\.?[A-Z0-9]{3}/?[A-Z0-9]{4}-?[0-9]{2}$";
    public static final String REGEX_CEP = "^[0-9]{5}[-]?[0-9]{3}$";

    // Confere se o campo bate com o regex
    private static boolean validarCampo(String campo, String regex) {
        if (campo == null) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(campo);
        return m.matches();
    }

    // Valida o formato do e-mail
    public static boolean validarEmail(String email) {
        return validarCampo(email, REGEX_EMAIL);
    }

    // Valida o formato do CNPJ
    public static boolean validarCnpj(String cnpj) {
        return validarCampo(cnpj, REGEX_CNPJ);
    }

    // Valida o formato do CEP
    public static boolean validarCep(String cep) {
        return validarCampo(cep, REGEX_CEP);
    }

    // Valida a regra da senha: 8 caracteres, com uma letra e um número
    public static boolean validarSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            return false;
        }

        boolean temLetra = false;
        boolean temNumero = false;

        for (char c : senha.toCharArray()) {
            if (Character.isLetter(c)) {
                temLetra = true;
            } else if (Character.isDigit(c)) {
                temNumero = true;
            }
        }

        return temLetra && temNumero;
    }
}
