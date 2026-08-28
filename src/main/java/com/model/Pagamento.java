package com.model;

import java.security.PrivateKey;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pagamento {

    private Integer id;                  // coluna: id
    private Float valor;                 // coluna: valor
    private LocalDateTime dataPagamento;     // coluna: data_pagamento
    private Boolean foiRealizado;        // coluna: foi_realizado
    private Integer fkContrato;          // coluna: fk_contrato_id
    private Integer fkMetodoPagamento;   // coluna: fk_metodo_pagamento_id

    // construtor

    public Pagamento(Integer id, Float valor, LocalDateTime dataPagamento, Boolean foiRealizado, Integer fkContrato, Integer fkMetodoPagamento) {
        this.id = id;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.foiRealizado = foiRealizado;
        this.fkContrato = fkContrato;
        this.fkMetodoPagamento = fkMetodoPagamento;
    }

    // gettes e setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Boolean getFoiRealizado() {
        return foiRealizado;
    }

    public void setFoiRealizado(Boolean foiRealizado) {
        this.foiRealizado = foiRealizado;
    }

    public Integer getFkContrato() {
        return fkContrato;
    }

    public void setFkContrato(Integer fkContrato) {
        this.fkContrato = fkContrato;
    }

    public Integer getFkMetodoPagamento() {
        return fkMetodoPagamento;
    }

    public void setFkMetodoPagamento(Integer fkMetodoPagamento) {
        this.fkMetodoPagamento = fkMetodoPagamento;
    }

    // toString

    @Override
    public String toString(){
        return "Pagamento{id=%d, valor=%f, dataPagamento=%s, foiRealizado=%b, fkContrato=%d, fkMetodoPagamento=%d}"
                .formatted(id, valor, dataPagamento, foiRealizado, fkContrato, fkMetodoPagamento);
    }
}
