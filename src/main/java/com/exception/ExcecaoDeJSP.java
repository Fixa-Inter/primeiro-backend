package com.exception;

public class ExcecaoDeJSP extends RuntimeException {
    public ExcecaoDeJSP(String message) {
        super(message);
    }

    //Excecoes de unique
    public static ExcecaoDeJSP uniqueDuplicado(String campo){
        String mensagem = "O %s já está cadastrado deve ser único.".formatted(campo);
        return new ExcecaoDeJSP(mensagem);
    }

    public static ExcecaoDeJSP emailDuplicado(){
        return uniqueDuplicado("email");
    }

    public static ExcecaoDeJSP cnpjDuplicado(){
        return uniqueDuplicado("CNPJ");
    }

    public static ExcecaoDeJSP dominioDuplicado(){
        return uniqueDuplicado("domínio de email");
    }

    //Excecoes de not null
    public static ExcecaoDeJSP notNullVazio(String campo){
        String mensagem = "O campo (%s) é obrigatório para continuar.".formatted(campo);
        return new ExcecaoDeJSP(mensagem);
    }

    public static ExcecaoDeJSP nomeVazio(){
        return notNullVazio("nome");
    }

    public static ExcecaoDeJSP emailVazio(){
        return notNullVazio("email");
    }

    public static ExcecaoDeJSP tipoInstituicaoVazio(){
        return notNullVazio("tipo de instituição");
    }

    public static ExcecaoDeJSP valorMensalVazio(){
        return notNullVazio("valor mensal");
    }

    public static ExcecaoDeJSP senhaVazio(){
        return notNullVazio("senha");
    }

    public static ExcecaoDeJSP statusVazio(){
        return notNullVazio("status");
    }

    public static ExcecaoDeJSP tipoAcessoVazio(){
        return notNullVazio("tipo de acesso");
    }

    public static ExcecaoDeJSP metodoPagamentoVazio(){
        return notNullVazio("método de pagamento");
    }
}
