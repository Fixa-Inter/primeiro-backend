package com.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    public static final String REGEX_EMAIL = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
    public static final String REGEX_CNPJ = "^[A-Z0-9]{2}\\.?[A-Z0-9]{3}\\.?[A-Z0-9]{3}/?[A-Z0-9]{4}-?[0-9]{2}$";
    public static final String REGEX_CEP = "^[0-9]{5}[-]?[0-9]{3}$";

    private static boolean validarCampo(String campo, String regex) {
        if (campo == null) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(campo);
        return m.matches();
    }

    public static boolean validarEmail(String email) {
        return validarCampo(email, REGEX_EMAIL);
    }

    public static boolean validarCnpj(String cnpj) {
        return validarCampo(cnpj, REGEX_CNPJ);
    }

    public static boolean validarCep(String cep) {
        return validarCampo(cep, REGEX_CEP);
    }
}
