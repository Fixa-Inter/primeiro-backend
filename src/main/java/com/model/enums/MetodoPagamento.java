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
    public static MetodoPagamento getNomeComBaseCodigo(int codigo) {
        return switch (codigo) {
            case 1 -> Credito;
            case 2 -> Debito;
            case 3 -> Pix;
            default -> throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
        };
    }

    // metodo para converter o valor para o codigo
    public static int getCodigoComBaseNome(String campo) {
        return switch (campo) {
            case Credito -> 1;
            case Debito -> 2;
            case Pix -> 3;
            default -> throw new IllegalArgumentException("Codigo de instituicao invalido: ");
        };
    }
}