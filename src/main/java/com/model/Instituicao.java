package com.model;

import com.model.enums.TipoInstituicao;

import java.time.LocalDate;

public class Instituicao {

    private Integer id;                          // coluna: id
    private String nome;                         // coluna: nome
    private String emailCorporativo;             // coluna: email_corporativo
    private LocalDate dataCadastro;              // coluna: data_cadastro
    private TipoInstituicao tipoDeInstituicao;   // coluna: tipo_de_instituicao
    private String dominioEmail;                 // coluna: dominioEmail

    // Construtor

    public Instituicao(Integer id, String nome, String emailCorporativo, LocalDate dataCadastro, TipoInstituicao tipoDeInstituicao, String dominioEmail) {
        this.id = id;
        this.nome = nome;
        this.emailCorporativo = emailCorporativo;
        this.dataCadastro = dataCadastro;
        this.tipoDeInstituicao = tipoDeInstituicao;
        this.dominioEmail = dominioEmail;
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

    public TipoInstituicao getTipoDeInstituicao() {
        return tipoDeInstituicao;
    }

    public void setTipoDeInstituicao(TipoInstituicao tipoDeInstituicao) {
        this.tipoDeInstituicao = tipoDeInstituicao;
    }

    public String getDominioEmail() {
        return dominioEmail;
    }

    public void setDominioEmail(String dominioEmail) {
        this.dominioEmail = dominioEmail;
    }

    // toString

    @Override
    public String toString(){
        return "Instituicao{id=%d, nome='%s', emailCorporativo='%s', dataCadastro=%s, dominioEmail=%s,tipoDeInstituicao=%s}"
                .formatted(id, nome, emailCorporativo, dataCadastro, dominioEmail, tipoDeInstituicao);
    }
}
