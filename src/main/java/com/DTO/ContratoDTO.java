package com.DTO;

import java.time.LocalDate;

/** Dados de contrato combinados com plano e instituição. */
public class ContratoDTO {
    private Integer id;
    private LocalDate dataInicio;
    private LocalDate dataVencimento;
    private String statusContrato;
    private String planoNome;
    private String estado;
    private String instituicaoNome;

    public ContratoDTO(Integer id, LocalDate dataInicio, LocalDate dataVencimento, String statusContrato, String planoNome, String estado, String instituicaoNome) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
        this.statusContrato = statusContrato;
        this.planoNome = planoNome;
        this.estado = estado;
        this.instituicaoNome = instituicaoNome;
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

    public String getStatusContrato() {
        return statusContrato;
    }

    public void setStatusContrato(String statusContrato) {
        this.statusContrato = statusContrato;
    }

    public String getPlanoNome() {
        return planoNome;
    }

    public void setPlanoNome(String planoNome) {
        this.planoNome = planoNome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInstituicaoNome() {
        return instituicaoNome;
    }

    public void setInstituicaoNome(String instituicaoNome) {
        this.instituicaoNome = instituicaoNome;
    }
}
