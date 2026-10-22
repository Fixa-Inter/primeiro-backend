package com.model;

import java.time.LocalDateTime;

public class Endereco {

    private Integer id;                 // coluna: id
    private String nome;                // coluna: nome
    private String rua;                 // coluna: rua
    private String bairro;              // coluna: bairro
    private String complemento;         // coluna: complemento
    private String cidade;              // coluna: cidade
    private String estado;              // coluna: estado
    private String numero;              // coluna: numero
    private String cep;                 // coluna: cep
    private String cnpj;                // coluna: cnpj
    private LocalDateTime dataCriacao;  // coluna: data
    private Integer fkInstituicao;      // coluna: fk_instituicao_id

    // construtor

    public Endereco(Integer id, String rua, String bairro, String complemento, String cidade, String estado, String numero, String cep, String cnpj, LocalDateTime dataCriacao, Integer fkInstituicao) {
        this.id = id;
        this.rua = rua;
        this.bairro = bairro;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
        this.numero = numero;
        this.cep = cep;
        this.cnpj = cnpj;
        this.dataCriacao = dataCriacao;
        this.fkInstituicao = fkInstituicao;
    }

    public Endereco(Integer id, String nome, String rua, String bairro, String complemento, String cidade, String estado, String numero, String cep, String cnpj, LocalDateTime dataCriacao, Integer fkInstituicao) {
        this(id, rua, bairro, complemento, cidade, estado, numero, cep, cnpj, dataCriacao, fkInstituicao);
        this.nome = nome;
    }

    public Endereco(String rua, String bairro, String complemento, String cidade, String estado, String numero, String cep, String cnpj, Integer fkInstituicao) {
        this.rua = rua;
        this.bairro = bairro;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
        this.numero = numero;
        this.cep = cep;
        this.cnpj = cnpj;
        this.fkInstituicao = fkInstituicao;
    }

    public Endereco(String nome, String rua, String bairro, String complemento, String cidade, String estado, String numero, String cep, String cnpj, Integer fkInstituicao) {
        this(rua, bairro, complemento, cidade, estado, numero, cep, cnpj, fkInstituicao);
        this.nome = nome;
    }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
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

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    // toString

    @Override
    public String toString(){
        return "%s %s %s %s %s %s %s %s %s %s %s %s"
                .formatted(id, nome, rua, bairro, complemento, cidade, estado, numero, cep, fkInstituicao, cnpj, dataCriacao);
    }
}
