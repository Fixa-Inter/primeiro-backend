package model;

import java.time.LocalDate;

public class Contrato {

    private Integer id;                // coluna: id
    private LocalDate dataInicio;      // coluna: data_inicio
    private LocalDate dataVencimento;  // coluna: data_vencimento
    private Integer fkPlano;           // coluna: fk_plano_id

    // construtor

    public Contrato(Integer id, LocalDate dataInicio, LocalDate dataVencimento, Integer fkPlano) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
        this.fkPlano = fkPlano;
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

    // toString

    @Override
    public String toString(){
        return "Contrato{id=%d, dataInicio=%s, dataVencimento=%s, fkPlano=%d}"
                .formatted(id, dataInicio, dataVencimento, fkPlano);
    }
}
