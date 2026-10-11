package com.model;

import java.sql.Timestamp;

public class VerificacaoCadastro {

    private Integer id;
    private String codigoVerificacao;
    private Timestamp dataExpiracao;
    private String email;
    private Boolean estaValido;

    // construtor

    public VerificacaoCadastro(Integer id, String codigoVerificacao, Timestamp dataExpiracao, String email, Boolean estaValido) {
        this.id = id;
        this.codigoVerificacao = codigoVerificacao;
        this.dataExpiracao = dataExpiracao;
        this.email = email;
        this.estaValido = estaValido;
    }

    public VerificacaoCadastro(String codigoVerificacao, String email) {
        this.codigoVerificacao = codigoVerificacao;
        this.email = email;
    }

    // getters e setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public Timestamp getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Timestamp dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEstaValido() {
        return estaValido;
    }

    public void setEstaValido(Boolean estaValido) {
        this.estaValido = estaValido;
    }

    // toString

    @Override
    public String toString(){
        return "%s %s %s %s %s"
                .formatted(id, codigoVerificacao, dataExpiracao, email, estaValido);
    }
}
