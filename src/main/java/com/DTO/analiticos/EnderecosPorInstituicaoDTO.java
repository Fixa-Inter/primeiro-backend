package com.DTO.analiticos;

public class EnderecosPorInstituicaoDTO {
    private String instituicao;
    private int enderecos;
    private int percentual;

    public EnderecosPorInstituicaoDTO(String instituicao, int enderecos, int percentual) {
        this.instituicao = instituicao;
        this.enderecos = enderecos;
        this.percentual = percentual;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public int getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(int enderecos) {
        this.enderecos = enderecos;
    }

    public int getPercentual() {
        return percentual;
    }
}
