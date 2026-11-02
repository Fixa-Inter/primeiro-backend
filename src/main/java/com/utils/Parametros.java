package com.utils;

import com.exception.ExcecaoDeJSP;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// Lê e valida os parâmetros da requisição
public class Parametros {

    // Classe utilitária, não deve ser instanciada
    private Parametros() {
    }

    // Lê um texto obrigatório
    public static String obrigatorio(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            throw ExcecaoDeJSP.notNullVazio(nome);
        }

        return valor.trim();
    }

    // Lê um inteiro obrigatório
    public static int inteiro(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome);

        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }

    // Lê um inteiro, ou null se vier vazio
    public static Integer inteiroOpcional(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return inteiro(request, nome);
    }

    // Lê um decimal obrigatório
    public static double decimal(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome).replace(',', '.');

        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }

    // Lê um valor monetário obrigatório
    public static BigDecimal bigDecimal(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome).replace(',', '.');

        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }

    // Lê uma data obrigatória
    public static LocalDate data(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome);

        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }

    // Lê uma data, ou null se vier vazia
    public static LocalDate dataOpcional(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return data(request, nome);
    }

    // Lê uma data obrigatória que não pode estar no futuro
    public static LocalDate dataNaoFutura(HttpServletRequest request, String nome) {
        LocalDate valor = data(request, nome);

        if (valor.isAfter(LocalDate.now())) {
            throw ExcecaoDeJSP.dataNoFuturo(nome);
        }

        return valor;
    }

    // Lê uma data que não pode estar no futuro, ou null se vier vazia
    public static LocalDate dataNaoFuturaOpcional(HttpServletRequest request, String nome) {
        LocalDate valor = dataOpcional(request, nome);

        if (valor != null && valor.isAfter(LocalDate.now())) {
            throw ExcecaoDeJSP.dataNoFuturo(nome);
        }

        return valor;
    }

    // Lê um texto obrigatório
    public static String obrigatorio(HttpServletRequest request, String nome, int tamanhoMaximo) {
        String valor = obrigatorio(request, nome);

        if (valor.length() > tamanhoMaximo) {
            throw ExcecaoDeJSP.tamanhoExcedido(nome, tamanhoMaximo);
        }

        return valor;
    }

    // Lê uma senha obrigatória e dentro da regra, sem trim
    public static String senha(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            throw ExcecaoDeJSP.notNullVazio(nome);
        }

        if (!RegexUtils.validarSenha(valor)) {
            throw ExcecaoDeJSP.senhaFraca();
        }

        return valor;
    }

    // Lê a senha da edição: em branco devolve null, preenchida segue a regra
    public static String senhaOpcional(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return senha(request, nome);
    }

    // Lê um valor booleano
    public static boolean booleano(HttpServletRequest request, String nome) {
        return Boolean.parseBoolean(request.getParameter(nome));
    }
}
