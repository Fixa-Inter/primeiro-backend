package com.model.enums;

// Métodos de pagamento aceitos
public enum MetodoPagamento {

    CREDITO("Crédito", 1),
    DEBITO("Débito", 2),
    PIX("Pix", 3);

    private final String nome;
    private final int codigo;

    MetodoPagamento(String nome, int codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    // Devolve o nome a partir do código
    public static String getNomeComBaseCodigo(int codigo) {

        for (MetodoPagamento metodoPagamento : MetodoPagamento.values()) {
            if (codigo == metodoPagamento.codigo) {
                return metodoPagamento.nome;
            }
        }

        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }

    // Converte o nome no enum
    public static MetodoPagamento converterEnum(String nome) {
        for (MetodoPagamento metodoPagamento : MetodoPagamento.values()) {
            if (nome.toLowerCase().equals(metodoPagamento.nome.toLowerCase())) {
                return metodoPagamento;
            }
        }

        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }

    // Converte o código no enum
    public static MetodoPagamento converterEnum(int codigo) {
        for (MetodoPagamento metodoPagamento : MetodoPagamento.values()) {
            if (codigo == metodoPagamento.codigo) {
                return metodoPagamento;
            }
        }

        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }
}