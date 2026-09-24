package com.model.enums;

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

    // metodo para converter o codigo para o valor
    public static String getNomeComBaseCodigo(int codigo) {

        for (TipoAcesso tipoAcesso : TipoAcesso.values()) {
            if (codigo == tipoAcesso.codigo) {
                return tipoAcesso.nome;
            }
        }

        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }

    // metodo para converter o valor para o codigo
    public static TipoAcesso converterEnum(String nome) {
        for (TipoAcesso tipoAcesso : TipoAcesso.values()) {
            if (nome.toLowerCase().equals(tipoAcesso.nome.toLowerCase())) {
                return tipoAcesso;
            }
        }

        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }

    public static TipoAcesso converterEnum(int codigo) {
        for (TipoAcesso tipoAcesso : TipoAcesso.values()) {
            if (codigo == tipoAcesso.getCodigo()) {
                return tipoAcesso;
            }
        }

        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }

}
