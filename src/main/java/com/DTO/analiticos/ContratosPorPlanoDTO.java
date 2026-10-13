package com.DTO.analiticos;

public class ContratosPorPlanoDTO {
    private String plano;
    private int contratos;
    private int percentual;

    public ContratosPorPlanoDTO(String plano, int contratos, int percentual) {
        this.plano = plano;
        this.contratos = contratos;
        this.percentual = percentual;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    public int getContratos() {
        return contratos;
    }

    public void setContratos(int contratos) {
        this.contratos = contratos;
    }
}
