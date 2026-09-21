package com.model.enums;

public enum TipoAcesso {

    Adiministrador(1),
    Gestor(2),
    Tecnico(3),
    Solicitante(4);

    final int codigo;

    TipoAcesso(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    // metodo para converter o codigo para o valor
    public static TipoInstituicao getNomeComBaseCodigo(int codigo) {
        return switch (codigo) {
            case 1 -> Adiministrador;
            case 2 -> Gestor;
            case 3 -> Tecnico;
            case 4 -> Solicitante;
            default -> throw new IllegalArgumentException("Tipo de instituicao invalido: ");
        };
    }
}
