package model;

import java.time.LocalDate;

public class Contrato {

    private Integer id;                // coluna: id
    private LocalDate dataInicio;      // coluna: data_inicio
    private LocalDate dataVencimento;  // coluna: data_vencimento
    private Integer fkPlano;           // coluna: fk_plano_id
    private Integer fkInstituicao;     // coluna: fk_instituicao_id
    private Boolean estaVigente;       // coluna: esta_vigente

    // construtor

    public Contrato(Integer id, LocalDate dataInicio, LocalDate dataVencimento, Integer fkPlano, Integer fkInstituicao ,Boolean estaVigente) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
        this.fkPlano = fkPlano;
        this.fkInstituicao = fkInstituicao;
        this.estaVigente = estaVigente;
    }


    // getters e setters

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

    public Integer getFkInstituicao() {
        return fkInstituicao;
    }

    public void setFkInstituicao(Integer fkInstituicao) {
        this.fkInstituicao = fkInstituicao;
    }

    public Boolean getEstaVigente() {
        return estaVigente;
    }

    public void setEstaVigente(Boolean estaVigente) {
        this.estaVigente = estaVigente;
    }

    // toString

    @Override
    public String toString(){
        return "Contrato{id=%d, dataInicio=%s, dataVencimento=%s, fkPlano=%d, fkInstituicao=%d,estaVigente=%b}"
                .formatted(id, dataInicio, dataVencimento, fkPlano, fkInstituicao, estaVigente);
    }
}
