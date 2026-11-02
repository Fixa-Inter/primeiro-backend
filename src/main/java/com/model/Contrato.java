package com.model;

import com.model.enums.StatusContrato;

import java.time.LocalDate;

// Representa a tabela contrato
public class Contrato {

    private Integer id;                       // coluna: id
    private LocalDate dataInicio;             // coluna: data_inicio
    private LocalDate dataVencimento;         // coluna: data_vencimento
    private Integer fkPlano;                  // coluna: fk_plano_id
    private Integer fkEndereco;               // coluna: fk_endereco_id
    private StatusContrato statusContrato;    // coluna: status_contrato

    public Contrato(Integer id, LocalDate dataInicio, LocalDate dataVencimento, Integer fkPlano, Integer fkEndereco, StatusContrato statusContrato) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
        this.fkPlano = fkPlano;
        this.fkEndereco = fkEndereco;
        this.statusContrato = statusContrato;
    }

    public Contrato(LocalDate dataVencimento, Integer fkPlano, Integer fkEndereco, StatusContrato statusContrato) {
        this.dataVencimento = dataVencimento;
        this.fkPlano = fkPlano;
        this.fkEndereco = fkEndereco;
        this.statusContrato = statusContrato;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Integer getFkPlano() {
        return fkPlano;
    }

    public void setFkPlano(Integer fkPlano) {
        this.fkPlano = fkPlano;
    }

    public StatusContrato getStatusContrato() {
        return statusContrato;
    }

    public void setStatusContrato(StatusContrato statusContrato) {
        this.statusContrato = statusContrato;
    }

    public Integer getFkEndereco() {
        return fkEndereco;
    }

    public void setFkEndereco(Integer fkEndereco) {
        this.fkEndereco = fkEndereco;
    }

    @Override
    public String toString(){
        return "%s %s %s %s %s %s"
                .formatted(id, dataInicio, dataVencimento, fkPlano, fkEndereco, statusContrato);
    }
}
