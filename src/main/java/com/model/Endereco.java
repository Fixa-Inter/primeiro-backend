package com.model;

public class Endereco {

    private Integer id;              // coluna: id
    private String rua;              // coluna: rua
    private String bairro;           // coluna: bairro
    private String complemento;      // coluna: complemento
    private String cidade;           // coluna: cidade
    private String estado;           // coluna: estado
    private Integer numero;          // coluna: numero
    private String cep;              // coluna: cep
    private Integer fkInstituicao;   // coluna: fk_instituicao_id

    // construtor

    public Endereco(Integer id, String rua, String bairro, String complemento, String cidade, String estado, Integer numero, String cep, Integer fkInstituicao) {
        this.id = id;
        this.rua = rua;
        this.bairro = bairro;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
        this.numero = numero;
        this.cep = cep;
        this.fkInstituicao = fkInstituicao;
    }

    // getters e setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplementp(String complementp) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getFkInstituicao() {
        return fkInstituicao;
    }

    public void setFkInstituicao(Integer fkInstituicao) {
        this.fkInstituicao = fkInstituicao;
    }

    // toString

    @Overridex
    public String toString(){
        return "Endereco{id=%d, rua='%s', bairro='%s', complemento='%s', cidade='%s', estado='%s', numero='%s', cep='%s'}"
                .formatted(id, rua, bairro, complemento, cidade, estado, numero, cep, fkInstituicao);
    }
}
