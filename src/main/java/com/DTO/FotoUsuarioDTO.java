package com.DTO;

import java.time.LocalDate;

/** Dados da foto acompanhados dos dados de identificação do usuário. */
public class FotoUsuarioDTO {
    private Integer id;
    private LocalDate dataRegistro;
    private String url;
    private String usuarioNome;
    private String usuarioEmail;

    public FotoUsuarioDTO(Integer id, LocalDate dataRegistro, String url, String usuarioNome, String usuarioEmail) {
        this.id = id;
        this.dataRegistro = dataRegistro;
        this.url = url;
        this.usuarioNome = usuarioNome;
        this.usuarioEmail = usuarioEmail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }
}
