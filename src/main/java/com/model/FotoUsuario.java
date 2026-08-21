package com.model;

import java.time.LocalDate;

public class FotoUsuario {

    private Integer id;              // coluna: id
    private LocalDate dataRegistro;  // coluna: data_registro
    private String url;              // coluna: url
    private Integer fkUsuario;       // coluna: fk_usuario_id

    // construtor

    public FotoUsuario(Integer id, LocalDate dataRegistro, String url, Integer fkUsuario) {
        this.id = id;
        this.dataRegistro = dataRegistro;
        this.url = url;
        this.fkUsuario = fkUsuario;
    }

    // getters e setters

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

    public Integer getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(Integer fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    // toString

    @Override
    public String toString(){
        return "FotoUsuario{id=%d, dataRegistro=%s, url='%s', fkUsuario=%d}"
                .formatted(id, dataRegistro, url, fkUsuario);
    }
}
