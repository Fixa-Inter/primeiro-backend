package com.model;

public class MetodoPagamento {

    private Integer id;        // coluna: id
    private String descricao;  // coluna: descricao

    // construtor

    public MetodoPagamento(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // getters e setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // toString

    @Override
    public String toString(){
        return "MetodoPagamento{id=%d, descricao='%s'}"
                .formatted(id, descricao);
    }
}
