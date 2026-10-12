package com.DTO.analiticos;

public class ContratosPorPlanoDTO {
    private String plano;
    private int contratos;

    public ContratosPorPlanoDTO(String plano, int contratos) {
        this.plano = plano;
        this.contratos = contratos;
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
