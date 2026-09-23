package com.model;

import com.model.enums.TipoAcesso;

import java.time.LocalDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Usuario {

    private Integer id;                     // coluna: id
    private String nome;                    // coluna: nome
    private String senhaHash;               // coluna: senha_hash
    private Boolean estaAtivo;              // coluna: esta_Ativo
    private String email;                   // coluna: email
    private LocalDateTime dataCriacao;          // coluna: data_criacao
    private String cargo;                   // coluna: cargo
    private TipoAcesso tipoDeAcesso;        // coluna: tipo_de_acesso
    private Integer fkEndereco;             // coluna: fk_endereco_id
    private LocalDate dataAniversario;  // coluna: data_nascimento
    private Boolean primeiroAcesso;         // coluna: primeiro acesso


    // construtor

    public Usuario(Integer id, String nome, String senhaHash, Boolean estaAtivo, String email, LocalDateTime dataCriacao, String cargo, TipoAcesso tipoDeAcesso, Integer fkEndereco, LocalDate dataAniversario, Boolean primeiroAcesso) {
        this.id = id;
        this.nome = nome;
        this.senhaHash = senhaHash;
        this.estaAtivo = estaAtivo;
        this.email = email;
        this.dataCriacao = dataCriacao;
        this.cargo = cargo;
        this.tipoDeAcesso = tipoDeAcesso;
        this.fkEndereco = fkEndereco;
        this.dataAniversario = dataAniversario;
        this.primeiroAcesso = primeiroAcesso;
    }


    // getters e setters

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

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public Boolean getEstaAtivo() {
        return estaAtivo;
    }

    public void setEstaAtivo(Boolean estaAtivo) {
        this.estaAtivo = estaAtivo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public TipoAcesso getTipoDeAcesso() {
        return tipoDeAcesso;
    }

    public void setTipoDeAcesso(TipoAcesso tipoDeAcesso) {
        this.tipoDeAcesso = tipoDeAcesso;
    }

    public Integer getFkEndereco() {
        return fkEndereco;
    }

    public void setFkEndereco(Integer fkEndereco) {
        this.fkEndereco = fkEndereco;
    }

    public LocalDate getDataAniversario() {
        return dataAniversario;
    }

    public void setDataAniversario(LocalDate dataAniversario) {
        this.dataAniversario = dataAniversario;
    }

    public Boolean getPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public void setPrimeiroAcesso(Boolean primeiroAcesso) {
        this.primeiroAcesso = primeiroAcesso;
    }

    // toString

    @Override
    public String toString(){
        return "Usuario{id=%d, nome='%s', senhaHash='%s', estaAtivo=%b, email='%s', dataCriacao=%s, cargo='%s', tipoDeAcesso='%s', fkTipoDeAcesso='%d', data_nascimento='%s', primeiro_acesso='%b'"
                .formatted(id, nome, senhaHash, estaAtivo, email, dataCriacao, cargo, tipoDeAcesso, fkEndereco,dataAniversario, primeiroAcesso);
        }
}
