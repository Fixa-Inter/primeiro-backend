package com.model.enums;

public enum MetodoPagamento {

    Credito(1),
    Debito(2),
    Pix(3);

    private final int codigo;

    MetodoPagamento(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    // metodo para converter o codigo para o valor
    public static TipoInstituicao getNomeComBaseCodigo(int codigo) {
        return switch (codigo) {
            case 1 -> Credito;
            case 2 -> Debito;
            case 3 -> Pix;
            default -> throw new IllegalArgumentException("Tipo de instituicao invalido: ");
        };
    }
}
