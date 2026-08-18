package model;

import java.time.LocalDate;

import java.time.LocalDate;

public class Usuario {

    private Integer id;                 // coluna: id
    private String nome;                // coluna: nome
    private String senhaHash;           // coluna: senha_hash
    private Boolean estaAtivo;          // coluna: esta_Ativo
    private String email;               // coluna: email
    private LocalDate dataCriacao;      // coluna: data_criacao
    private String cargo;               // coluna: cargo
    private String tipoDeAcesso;        // coluna: tipo_de_acesso
    private Integer fkInstituicao;      // coluna: fk_instituicao_id
    private LocalDate dataUltimoAcesso; // coluna: data_ultimo_acesso


    // construtor

    public Usuario(Integer id, String nome, String senhaHash, Boolean estaAtivo, String email, LocalDate dataCriacao, String cargo, String tipoDeAcesso, Integer fkInstituicao, LocalDate dataUltimoAcesso) {
        this.id = id;
        this.nome = nome;
        this.senhaHash = senhaHash;
        this.estaAtivo = estaAtivo;
        this.email = email;
        this.dataCriacao = dataCriacao;
        this.cargo = cargo;
        this.tipoDeAcesso = tipoDeAcesso;
        this.fkInstituicao = fkInstituicao;
        this.dataUltimoAcesso = dataUltimoAcesso;
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

    public String getTipoDeAcesso() {
        return tipoDeAcesso;
    }

    public void setTipoDeAcesso(String tipoDeAcesso) {
        this.tipoDeAcesso = tipoDeAcesso;
    }

    public Integer getFkInstituicao() {
        return fkInstituicao;
    }

    public void setFkInstituicao(Integer fkInstituicao) {
        this.fkInstituicao = fkInstituicao;
    }

    public LocalDate getDataUltimoAcesso() {
        return dataUltimoAcesso;
    }

    public void setDataUltimoAcesso(LocalDate dataUltimoAcesso) {
        this.dataUltimoAcesso = dataUltimoAcesso;
    }

    // toString

    @Override
    public String toString(){
        return "Usuario{id=%d, nome='%s', senhaHash='%s', estaAtivo=%b, email='%s', dataCriacao=%s, cargo='%s', tipoDeAcesso='%s', fkTipoDeAcesso=%d, dataUltimoAcesso=%s}"
                .formatted(id, nome, senhaHash, estaAtivo, email, dataCriacao, cargo, tipoDeAcesso, fkInstituicao, dataUltimoAcesso);
        }
}
