package com.model.enums;

public enum StatusContrato {

    ATIVO("Ativo", 1),
    INATIVO("Inativo", 2),
    CANCELADO("Cancelado", 3);

    final String nome;
    final int codigo;

    StatusContrato( String nome,int codigo) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    // metodo para converter o codigo para o valor
    public static String getNomeComBaseCodigo(int codigo) {
        for (StatusContrato statusContrato : StatusContrato.values()) {
            if (codigo == statusContrato.codigo) {
                return statusContrato.nome;
            }
        }

        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }

    // metodo para converter o enum
    public static StatusContrato converterEnum(String nome) {
        for (StatusContrato statusContrato : StatusContrato.values()) {
            if (nome.toLowerCase().equals(statusContrato.nome.toLowerCase())) {
                return statusContrato;
            }
        }
        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }

    public static StatusContrato converterEnum(int codigo) {
        for (StatusContrato statusContrato : StatusContrato.values()) {
            if (codigo == statusContrato.codigo) {
                return statusContrato;
            }
        }
        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }
}
