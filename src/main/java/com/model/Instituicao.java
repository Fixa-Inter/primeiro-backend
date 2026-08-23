package com.model;

import java.time.LocalDate;

public class Instituicao {

    private Integer id;              // coluna: id
    private String nome;             // coluna: nome
    private Boolean estaAtivo;       // coluna: esta_ativo
    private String emailCorporativo; // coluna: email_corporativo
    private LocalDate dataCadastro;  // coluna: data_cadastro
    private String cnpj;             // coluna: cnpj

    // Construtor

    public Instituicao(Integer id, String nome, Boolean estaAtivo, String emailCorporativo, LocalDate dataCadastro, String cnpj) {
        this.id = id;
        this.nome = nome;
        this.estaAtivo = estaAtivo;
        this.emailCorporativo = emailCorporativo;
        this.dataCadastro = dataCadastro;
        this.cnpj = cnpj;
    }

    // Getters e setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getEstaAtivo() {
        return estaAtivo;
    }

    public void setEstaAtivo(Boolean estaAtivo) {
        this.estaAtivo = estaAtivo;
    }

    public String getEmailCorporativo() {
        return emailCorporativo;
    }

    public void setEmailCorporativo(String emailCorporativo) {
        this.emailCorporativo = emailCorporativo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    // toString

    @Override
    public String toString(){
        return "Instituicao{id=%d, nome='%s', estaAtivo=%b, emailCorporativo='%s', dataCadastro=%s, cnpj='%s'}"
                .formatted(id, nome, estaAtivo, emailCorporativo, dataCadastro, cnpj);
    }
}
