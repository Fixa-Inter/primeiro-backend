package model;

import java.time.LocalDate;

public class Usuario {

    private Integer id;               // coluna: id
    private String nome;              // coluna: nome
    private String senhaHash;         // coluna: senha_hash
    private Boolean estaAtivo;        // coluna: esta_Ativo
    private String email;             // coluna: email
    private LocalDate dataCriacao;    // coluna: data_criacao
    private String cargo;             // coluna: cargo
    private Integer fkInstituicao;    // coluna: fk_instituicao_id
    private  Integer fkTipoDeAcesso;  // coluna: k_tipo_de_acesso_id


    // construtor
    public Usuario(Integer id, String nome, String senhaHash, Boolean estaAtivo, String email, LocalDate dataCriacao, String cargo, Integer fkInstituicao, Integer fkTipoDeAcesso) {
        this.id = id;
        this.nome = nome;
        this.senhaHash = senhaHash;
        this.estaAtivo = estaAtivo;
        this.email = email;
        this.dataCriacao = dataCriacao;
        this.cargo = cargo;
        this.fkInstituicao = fkInstituicao;
        this.fkTipoDeAcesso = fkTipoDeAcesso;
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

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Integer getFkInstituicao() {
        return fkInstituicao;
    }

    public void setFkInstituicao(Integer fkInstituicao) {
        this.fkInstituicao = fkInstituicao;
    }

    public Integer getFkTipoDeAcesso() {
        return fkTipoDeAcesso;
    }

    public void setFkTipoDeAcesso(Integer fkTipoDeAcesso) {
        this.fkTipoDeAcesso = fkTipoDeAcesso;
    }

    // toString

    @Override
    public String toString(){
        return "Usuario{id=%d, nome='%s', senhaHash='%s', estaAtivo=%b, email='%s', dataCriacao=%s, cargo='%s', fkInstituicao=%d, fkTipoDeAcesso=%d}"
                .formatted(id, nome, senhaHash, estaAtivo, email, dataCriacao, cargo, fkInstituicao, fkInstituicao);
        }
}
