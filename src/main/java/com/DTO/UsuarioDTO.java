package com.DTO;

import java.time.LocalDate;

/** Dados de usuário combinados com informações de endereço e instituição. */
public class UsuarioDTO {
    private Integer id;
    private String nome;
    private String email;
    private String cargo;
    private String tipoAcesso;
    private Boolean estaAtivo;
    private LocalDate dataNascimento;
    private Boolean primeiroAcesso;
    private String estado;
    private String instituicaoNome;

    public UsuarioDTO(Integer id, String nome, String email, String cargo, String tipoAcesso, Boolean estaAtivo, LocalDate dataNascimento, Boolean primeiroAcesso, String estado, String instituicaoNome) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
        this.tipoAcesso = tipoAcesso;
        this.estaAtivo = estaAtivo;
        this.dataNascimento = dataNascimento;
        this.primeiroAcesso = primeiroAcesso;
        this.estado = estado;
        this.instituicaoNome = instituicaoNome;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(String tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }

    public Boolean getEstaAtivo() {
        return estaAtivo;
    }

    public void setEstaAtivo(Boolean estaAtivo) {
        this.estaAtivo = estaAtivo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Boolean getPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public void setPrimeiroAcesso(Boolean primeiroAcesso) {
        this.primeiroAcesso = primeiroAcesso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInstituicaoNome() {
        return instituicaoNome;
    }

    public void setInstituicaoNome(String instituicaoNome) {
        this.instituicaoNome = instituicaoNome;
    }
}
