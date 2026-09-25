package com.model.enums;

public enum TipoInstituicao {

    ESCOLA("Escola",1),
    FACULDADE("Faculdade",2),
    EMPRESA("Empresa",3),
    ORGAO_PUBLICO("Órgão público",4);

    private final String nome;
    private final int codigo;

    TipoInstituicao(String nome,int codigo){
        this.nome = nome;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getCodigo() {
        return codigo;
    }

    // metodo para converter o codigo para o valor
    public static String getNomeComBaseCodigo(int codigo) {
        for (TipoInstituicao tipoInstituicao : TipoInstituicao.values()) {
            if (codigo == tipoInstituicao.codigo) {
                return tipoInstituicao.nome;
            }
        }

        throw new IllegalArgumentException("Tipo de instituicao invalido");
    }

    // metodo para converter o valor para o codigo
    public static TipoInstituicao converterEnum(String nome) {
        for (TipoInstituicao tipoInstituicao : TipoInstituicao.values()) {
            if (nome.toLowerCase().equals(tipoInstituicao.nome.toLowerCase())) {
                return tipoInstituicao;
            }
        }

        throw new IllegalArgumentException("Codigo de tipo de instituicao invalido");
    }

    public static TipoInstituicao converterEnum(int codigo) {
        for (TipoInstituicao tipoInstituicao : TipoInstituicao.values()) {
            if (tipoInstituicao.codigo == codigo) {
                return tipoInstituicao;
            }
        }

        throw new IllegalArgumentException("Codigo de metodo de pagamento invalido");
    }
}
