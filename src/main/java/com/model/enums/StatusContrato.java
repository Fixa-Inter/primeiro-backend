package com.model.enums;

public enum StatusContrato {

    ATIVO("Ativo", 1),
    INATIVO(2),
    CANCELADO(3);

    final int codigo;

    StatusContrato(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }



}
