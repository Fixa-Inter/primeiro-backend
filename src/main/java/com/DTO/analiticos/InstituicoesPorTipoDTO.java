package com.DTO.analiticos;

import com.model.enums.TipoInstituicao;

public class InstituicoesPorTipoDTO {
    private String tipo;
    private Integer instituicoes;

    public InstituicoesPorTipoDTO(int tipo, Integer instituicoes) {
        this.tipo = TipoInstituicao.converterEnum(tipo).getNome();
        this.instituicoes = instituicoes;
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

    public void setInstituicoes(Integer instituicoes) {
        this.instituicoes = instituicoes;
    }
}
