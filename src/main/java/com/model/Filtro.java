package com.model;

import com.model.enums.OperacaoFiltro;

// Filtro de listagem: campo, valor e operação
public class Filtro {
    private String campoFiltravel;
    private Object valor;
    private OperacaoFiltro operacaoFiltro;

    // Cria o filtro com campo, valor e operação
    public Filtro(String campoFiltravel, Object valor, OperacaoFiltro operacaoFiltro) {
        this.campoFiltravel = campoFiltravel;
        this.valor = valor;
        this.operacaoFiltro = operacaoFiltro;
    }

    public String getCampoFiltravel() {
        return campoFiltravel;
    }

    public Object getValor() {
        return valor;
    }

    public OperacaoFiltro getOperacaoFiltro() {
        return operacaoFiltro;
    }
}
