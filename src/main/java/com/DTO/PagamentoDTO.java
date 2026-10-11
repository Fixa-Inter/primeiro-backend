package com.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Dados de pagamento combinados com contrato, plano e instituição. */
public class PagamentoDTO {
    private Integer id;
    private BigDecimal valor;
    private LocalDateTime dataPagamento;
    private Boolean foiRealizado;
    private String statusContrato;
    private LocalDate dataVencimento;
    private String planoNome;
    private String estado;
    private String instituicaoNome;
    private Boolean estaEmDia;

    public PagamentoDTO(Integer id, BigDecimal valor, LocalDateTime dataPagamento, Boolean foiRealizado, String statusContrato, LocalDate dataVencimento, String planoNome, String estado, String instituicaoNome, Boolean estaEmDia) {
        this.id = id;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.foiRealizado = foiRealizado;
        this.statusContrato = statusContrato;
        this.dataVencimento = dataVencimento;
        this.planoNome = planoNome;
        this.estado = estado;
        this.instituicaoNome = instituicaoNome;
        this.estaEmDia = estaEmDia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
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

    public String getStatusContrato() {
        return statusContrato;
    }

    public void setStatusContrato(String statusContrato) {
        this.statusContrato = statusContrato;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
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

    public Boolean getEstaEmDia() {
        return estaEmDia;
    }

    public void setEstaEmDia(Boolean estaEmDia) {
        this.estaEmDia = estaEmDia;
    }

    @Override
    public String toString() {
        return "%s %s %s %s %s %s %s %s %s %s"
                .formatted(id, valor, dataPagamento, foiRealizado, statusContrato, dataVencimento,
                        planoNome, estado, instituicaoNome, estaEmDia);
    }
}
