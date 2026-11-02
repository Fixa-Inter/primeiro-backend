package com.utils;

import com.exception.ExcecaoDeJSP;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parametros {

    private Parametros() {
    }

    // ---------- texto ----------

    public static String obrigatorio(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            throw ExcecaoDeJSP.notNullVazio(nome);
        }

        return valor.trim();
    }

    // ---------- numeros ----------

    public static int inteiro(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome);

        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }


    public static Integer inteiroOpcional(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return inteiro(request, nome);
    }

    public static double decimal(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome).replace(',', '.');

        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }


    public static BigDecimal bigDecimal(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome).replace(',', '.');

        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }

    // ---------- datas ----------

    public static LocalDate data(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome);

        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }

    public static LocalDate dataOpcional(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return data(request, nome);
    }

    // data que não pode estar no futuro (ex.: data de nascimento)
    public static LocalDate dataNaoFutura(HttpServletRequest request, String nome) {
        LocalDate valor = data(request, nome);

        if (valor.isAfter(LocalDate.now())) {
            throw ExcecaoDeJSP.dataNoFuturo(nome);
        }

        return valor;
    }

    public static LocalDate dataNaoFuturaOpcional(HttpServletRequest request, String nome) {
        LocalDate valor = dataOpcional(request, nome);

        if (valor != null && valor.isAfter(LocalDate.now())) {
            throw ExcecaoDeJSP.dataNoFuturo(nome);
        }

        return valor;
    }

    // ---------- texto com limite ----------

    public static String obrigatorio(HttpServletRequest request, String nome, int tamanhoMaximo) {
        String valor = obrigatorio(request, nome);

        if (valor.length() > tamanhoMaximo) {
            throw ExcecaoDeJSP.tamanhoExcedido(nome, tamanhoMaximo);
        }

        return valor;
    }

    // ---------- senha ----------

    // senha obrigatória e dentro da regra. Não usa trim: espaços fazem parte da senha
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

    // na edicao: em branco = manter a atual (devolve null); preenchida = precisa seguir a regra
    public static String senhaOpcional(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return senha(request, nome);
    }

    // ---------- booleano ----------

    public static boolean booleano(HttpServletRequest request, String nome) {
        return Boolean.parseBoolean(request.getParameter(nome));
    }
}
