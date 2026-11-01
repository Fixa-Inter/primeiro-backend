package com.utils;

import com.exception.ExcecaoDeJSP;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Leitura segura dos parametros que chegam dos formularios.
 *
 * Todos os metodos recebem o request e o NOME do parametro. Quando o valor esta
 * ausente, vazio ou invalido, lancam ExcecaoDeJSP com uma mensagem amigavel.
 * Os servlets ja capturam essa excecao no doPost e devolvem o usuario ao
 * formulario exibindo a mensagem (atributo "erro"), em vez de um erro 500.
 *
 * Metodos "...Opcional" devolvem null quando o parametro nao veio ou esta vazio,
 * e so lancam excecao se vier preenchido com um valor invalido.
 */
public class Parametros {

    private Parametros() {
    }

    // ---------- texto ----------

    /** Texto obrigatorio, sem espacos nas pontas. Vazio ou ausente lanca "preencha o campo". */
    public static String obrigatorio(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            throw ExcecaoDeJSP.notNullVazio(nome);
        }

        return valor.trim();
    }

    // ---------- numeros ----------

    /** Numero inteiro obrigatorio (ids, chaves estrangeiras, codigos de enum, duracao...). */
    public static int inteiro(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome);

        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }

    /** Numero inteiro opcional: null quando vazio ou ausente. */
    public static Integer inteiroOpcional(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return inteiro(request, nome);
    }

    /** Numero decimal obrigatorio (aceita ponto; virgula e trocada por ponto). */
    public static double decimal(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome).replace(',', '.');

        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }

    /** Valor monetario obrigatorio (BigDecimal), usado em pagamentos. */
    public static BigDecimal bigDecimal(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome).replace(',', '.');

        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }

    // ---------- datas ----------

    /** Data obrigatoria no formato yyyy-MM-dd (o que o input type="date" envia). */
    public static LocalDate data(HttpServletRequest request, String nome) {
        String valor = obrigatorio(request, nome);

        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            throw ExcecaoDeJSP.valorInvalido(nome);
        }
    }

    /** Data opcional: null quando vazia ou ausente. */
    public static LocalDate dataOpcional(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return data(request, nome);
    }

    // ---------- booleano ----------

    /**
     * Checkbox: o navegador so envia o campo quando esta marcado, entao
     * ausente significa false. Nao lanca excecao.
     */
    public static boolean booleano(HttpServletRequest request, String nome) {
        return Boolean.parseBoolean(request.getParameter(nome));
    }
}
