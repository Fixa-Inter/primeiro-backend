package com.DTO.analiticos;

import com.model.enums.TipoInstituicao;

public class InstituicoesPorTipoDTO {
    private String tipo;
    private Integer instituicoes;
    private int percentual;

    public InstituicoesPorTipoDTO(int tipo, Integer instituicoes, int percentual) {
        this.tipo = TipoInstituicao.converterEnum(tipo).getNome();
        this.instituicoes = instituicoes;
        this.percentual = percentual;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getInstituicoes() {
        return instituicoes;
    }

    public int getPercentual() {
        return percentual;
    }

    public void setInstituicoes(Integer instituicoes) {
        this.instituicoes = instituicoes;
    }
}
