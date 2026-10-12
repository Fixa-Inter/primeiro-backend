package com.DTO.analiticos;

public class EnderecosPorInstituicaoDTO {
    private String instituicao;
    private int enderecos;

    public EnderecosPorInstituicaoDTO(String instituicao, int enderecos) {
        this.instituicao = instituicao;
        this.enderecos = enderecos;
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
}
