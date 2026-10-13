package com.DTO.analiticos;

public class ResumoDTO {
    private int instituicoes;
    private int usuarios;
    private int enderecos;

    public ResumoDTO(int instituicoes, int usuarios, int enderecos) {
        this.instituicoes = instituicoes;
        this.usuarios = usuarios;
        this.enderecos = enderecos;
    }

    public int getInstituicoes() {
        return instituicoes;
    }

    public void setInstituicoes(int instituicoes) {
        this.instituicoes = instituicoes;
    }

    public int getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(int usuarios) {
        this.usuarios = usuarios;
    }

    public int getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(int enderecos) {
        this.enderecos = enderecos;
    }
}
