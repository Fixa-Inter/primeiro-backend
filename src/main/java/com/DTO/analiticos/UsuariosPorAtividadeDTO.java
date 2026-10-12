package com.DTO.analiticos;

public class UsuariosPorAtividadeDTO {
    private String atividade;
    private int usuarios;

    public UsuariosPorAtividadeDTO(boolean ativo, int usuarios) {
        if (ativo){
            this.atividade = "Ativo";
        }
        else{
            this.atividade = "Inativo";
        }
        this.usuarios = usuarios;
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
