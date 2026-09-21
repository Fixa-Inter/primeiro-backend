package com.model.enums;

public enum TipoInstituicao {

    Escola(1),
    Faculdade(2),
    Empresa(3),
    Orgao_Publico(4);

    private final int codigo;

    TipoInstituicao(int codigo){
        this.codigo = codigo;
    }

    public int getCodigo() {
        return Codigo;
    }

    // metodo para converter o codigo para o valor
    public static TipoInstituicao getNomeComBaseCodigo(int codigo) {
        return switch (codigo) {
            case 1 -> Escola;
            case 2 -> Faculdade;
            case 3 -> Empresa;
            case 4 -> Orgao_Publico;
            default -> throw new IllegalArgumentException("Tipo de instituicao invalido: ");
        };
    }

    // metodo para converter o valor para o codigo
    public static int getCodigoComBaseNome(String campo) {
        return switch (campo) {
            case Escola -> 1;
            case Faculdade -> 2;
            case Empresa -> 3;
            case Orgao_Publico -> 4;
            default -> throw new IllegalArgumentException("Codigo de instituicao invalido: ");
        };
    }

}
