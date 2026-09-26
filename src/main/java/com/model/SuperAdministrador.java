package com.model;

public class SuperAdministrador {

    private Integer id;                // coluna: id
    private String nome;               // coluna: nome
    private String senhaHash;          // coluna: senha_hash
    private String email;              // coluna: email

    // construtor

    public SuperAdministrador(Integer id, String nome, String senhaHash, String email) {
        this.id = id;
        this.nome = nome;
        this.senhaHash = senhaHash;
        this.email = email;
    }

    public SuperAdministrador(String nome, String senhaHash, String email) {
        this.nome = nome;
        this.senhaHash = senhaHash;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // toString

    @Override
    public String toString(){
        return "SuperAdministrador{id=%d, funcao='%s', nome='%s', senhaHash='%s', email='%s'}"
                .formatted(id, nome, senhaHash, email);
    }
}
