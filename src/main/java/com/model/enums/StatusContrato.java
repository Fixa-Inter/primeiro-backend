package com.model.enums;

// Situações possíveis de um contrato
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

    // Devolve o nome a partir do código
    public static String getNomeComBaseCodigo(int codigo) {
        for (StatusContrato statusContrato : StatusContrato.values()) {
            if (codigo == statusContrato.codigo) {
                return statusContrato.nome;
            }
        }

        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }

    // Converte o nome no enum
    public static StatusContrato converterEnum(String nome) {
        for (StatusContrato statusContrato : StatusContrato.values()) {
            if (nome.toLowerCase().equals(statusContrato.nome.toLowerCase())) {
                return statusContrato;
            }
        }
        throw new IllegalArgumentException("Status de contrato invalido");
    }

    // Converte o código no enum
    public static StatusContrato converterEnum(int codigo) {
        for (StatusContrato statusContrato : StatusContrato.values()) {
            if (codigo == statusContrato.codigo) {
                return statusContrato;
            }
        }
        throw new IllegalArgumentException("Codigo de status de contrato invalido");
    }
}
