package com.model;

import com.model.enums.OperacaoFiltro;

public class Filtro {
    private String campoFiltravel;
    private Object valor;
    private OperacaoFiltro operacaoFiltro;

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
