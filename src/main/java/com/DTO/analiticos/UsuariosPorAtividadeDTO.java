package com.DTO.analiticos;

public class UsuariosPorAtividadeDTO {
    private String atividade;
    private int usuarios;
    private int percentual;

    public UsuariosPorAtividadeDTO(boolean ativo, int usuarios, int percentual) {
        if (ativo){
            this.atividade = "Ativo";
        }
        else{
            this.atividade = "Inativo";
        }
        this.usuarios = usuarios;
        this.percentual = percentual;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public int getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(int usuarios) {
        this.usuarios = usuarios;
    }
}
