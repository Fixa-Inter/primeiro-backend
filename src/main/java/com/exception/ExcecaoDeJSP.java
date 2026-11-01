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

    // Constraint -> senha ou email diferentes do acesso ao banco de dados
    public static ExcecaoDeJSP falhaLogin() {
        return new ExcecaoDeJSP("Login falhou. Verifique se Email e Senha.");
    }

    //invalido no regex
    public static ExcecaoDeJSP valorInvalido(String campo) {
        String msg = "O valor informado para %s é inválido.".formatted(campo);
        return new ExcecaoDeJSP(msg);
    }

    //senha fora da regra
    public static ExcecaoDeJSP senhaFraca() {
        return new ExcecaoDeJSP(MENSAGEM_SENHA_FRACA);
    }

    public static final String MENSAGEM_SENHA_FRACA =
            "A senha deve ter no mínimo 8 caracteres, com pelo menos uma letra e um número.";

    //texto maior que o limite
    public static ExcecaoDeJSP tamanhoExcedido(String campo, int maximo) {
        String msg = "O campo %s deve ter no máximo %d caracteres.".formatted(campo, maximo);
        return new ExcecaoDeJSP(msg);
    }

    //vencimento do contrato antes da data de inicio
    public static ExcecaoDeJSP vencimentoAntesDoInicio() {
        return new ExcecaoDeJSP("A data de vencimento não pode ser anterior à data de início do contrato.");
    }

    //data que nao pode estar no futuro
    public static ExcecaoDeJSP dataNoFuturo(String campo) {
        String msg = "A data informada para %s não pode estar no futuro.".formatted(campo);
        return new ExcecaoDeJSP(msg);
    }
}
