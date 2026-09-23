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
    public static TipoAcesso getNomeComBaseCodigo(int codigo) {
        return switch (codigo) {
            case 1 -> Adiministrador;
            case 2 -> Gestor;
            case 3 -> Tecnico;
            case 4 -> Solicitante;
            default -> throw new IllegalArgumentException("Tipo de acesso invalido");
        };
    }

    // metodo para converter o valor para o codigo
    public static int getCodigoComBaseNome(String campo) {
        return switch (campo) {
            case Adiministrador -> 1;
            case Gestor -> 2;
            case Tecnico -> 3;
            case Solicitante -> 4;
            default -> throw new IllegalArgumentException("Codigo de tipo de acesso invalido");
        };
    }

}
