package com.model.enums;

public enum StatusContrato {

    Ativo(1),
    Inativo(2),
    Cancelado(3);

    final int codigo;

    StatusContrato(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    // metodo para converter o codigo para o valor
    public static StatusContrato getNomeComBaseCodigo(int codigo) {
        return switch (codigo) {
            case 1 -> Ativo;
            case 2 -> Inativo;
            case 3 -> Cancelado;
            default -> throw new IllegalArgumentException("Tipo de instituicao invalido: ");
        };
    }

    // metodo para converter o valor para o codigo
    public static int getCodigoComBaseNome(String campo) {
        return switch (campo) {
            case Ativo -> 1;
            case Inativo -> 2;
            case Cancelado -> 3;
            default -> throw new IllegalArgumentException("Codigo de instituicao invalido: ");
        };
    }

}
