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
        String mensagem = "Por favor, preencha o campo %s.".formatted(campo);
        return new ExcecaoDeJSP(mensagem);
    }
}
