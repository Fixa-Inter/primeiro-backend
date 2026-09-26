package com.model.enums;

public enum OperacaoFiltro {
    IGUAL("Igual","="),
    MAIOR_QUE("Maior que",">"),
    MAIOR_OU_IGUAL("Maior ou igual",">="),
    MENOR_QUE("Menor que","<"),
    MENOR_OU_IGUAL("Menor ou igual","<="),
    CONTEM("Contêm","ILIKE");

    private final String nome;
    private final String operadorSQL;

    OperacaoFiltro(String nome, String operadorSQL) {
        this.nome = nome;
        this.operadorSQL = operadorSQL;
    }

    public String getNome() {
        return nome;
    }

    public String getOperadorSQL() {
        return operadorSQL;
    }

    public static OperacaoFiltro converterEnumPorNome(String nome) {
        for (OperacaoFiltro operacaoFiltro : OperacaoFiltro.values()) {
            if (nome.toLowerCase().equals(operacaoFiltro.nome.toLowerCase())) {
                return operacaoFiltro;
            }
        }
        throw new IllegalArgumentException("Operacao de filtro invalida");
    }

    public static OperacaoFiltro converterEnumPorOperador(String operador) {
        for (OperacaoFiltro operacaoFiltro : OperacaoFiltro.values()) {
            if (operador.equalsIgnoreCase(operacaoFiltro.operadorSQL)) {
                return operacaoFiltro;
            }
        }
        throw new IllegalArgumentException("Codigo de operacao de filtro invalida invalido");
    }
}
