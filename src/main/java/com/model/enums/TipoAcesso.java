package com.model.enums;

// Níveis de acesso de um usuário
public enum TipoAcesso {

    ADMINISTRADOR("Administrador",1),
    GESTOR("Gestor",2),
    TECNICO("Técnico",3),
    SOLICITANTE("Solicitante",4);

    final String nome;
    final int codigo;

    TipoAcesso(String nome, int codigo) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getCodigo() {
        return codigo;
    }

    // Devolve o nome a partir do código
    public static String getNomeComBaseCodigo(int codigo) {

        for (TipoAcesso tipoAcesso : TipoAcesso.values()) {
            if (codigo == tipoAcesso.codigo) {
                return tipoAcesso.nome;
            }
        }

        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }

    // Converte o nome no enum
    public static TipoAcesso converterEnum(String nome) {
        for (TipoAcesso tipoAcesso : TipoAcesso.values()) {
            if (nome.toLowerCase().equals(tipoAcesso.nome.toLowerCase())) {
                return tipoAcesso;
            }
        }

        throw new IllegalArgumentException("Tipo de acesso invalido");
    }

    // Converte o código no enum
    public static TipoAcesso converterEnum(int codigo) {
        for (TipoAcesso tipoAcesso : TipoAcesso.values()) {
            if (codigo == tipoAcesso.getCodigo()) {
                return tipoAcesso;
            }
        }

        throw new IllegalArgumentException("Codigo de tipo de acesso invalido");
    }

}
