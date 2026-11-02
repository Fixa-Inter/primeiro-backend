package com.exception;

import java.util.Map;

public class ExcecaoDeJSP extends RuntimeException {
    public ExcecaoDeJSP(String message) {
        super(message);
    }

    // nome de cada campo como aparece na tela (o servlet passa o nome do parâmetro, ex: fkEndereco)
    private static final Map<String, String> ROTULOS = Map.ofEntries(
            Map.entry("id", "ID"),
            Map.entry("nome", "nome"),
            Map.entry("email", "e-mail"),
            Map.entry("emailCorporativo", "e-mail corporativo"),
            Map.entry("senha", "senha"),
            Map.entry("cnpj", "CNPJ"),
            Map.entry("cep", "CEP"),
            Map.entry("url", "URL"),
            Map.entry("fkEndereco", "endereço"),
            Map.entry("fkInstituicao", "instituição"),
            Map.entry("fkPlano", "plano"),
            Map.entry("fkUsuario", "usuário"),
            Map.entry("fkContrato", "contrato"),
            Map.entry("tipoInstituicao", "tipo de instituição"),
            Map.entry("tipoDeAcesso", "tipo de acesso"),
            Map.entry("statusContrato", "status do contrato"),
            Map.entry("metodoPagamento", "método de pagamento"),
            Map.entry("dataVencimento", "data de vencimento"),
            Map.entry("dataAniversario", "data de aniversário"),
            Map.entry("valorMensal", "valor mensal"),
            Map.entry("duracaoMeses", "duração em meses"),
            Map.entry("valor", "valor"),
            Map.entry("foiRealizado", "pagamento realizado"),
            Map.entry("estaAtivo", "usuário ativo"),
            Map.entry("primeiroAcesso", "primeiro acesso")
    );

    // devolve o nome amigável do campo; se não estiver na lista, separa o camelCase em palavras (dataInicio vira "data inicio")
    private static String rotulo(String campo) {
        String rotulo = ROTULOS.get(campo);
        if (rotulo != null) {
            return rotulo;
        }
        return campo.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
    }

    // Exceções de unique
    public static ExcecaoDeJSP uniqueDuplicado(String campo){
        String mensagem = "Este %s já está cadastrado.".formatted(campo);
        return new ExcecaoDeJSP(mensagem);
    }

    public static ExcecaoDeJSP emailDuplicado(){
        return uniqueDuplicado("e-mail");
    }

    public static ExcecaoDeJSP cnpjDuplicado(){
        return uniqueDuplicado("CNPJ");
    }

    public static ExcecaoDeJSP dominioDuplicado(){
        return uniqueDuplicado("domínio de e-mail");
    }

    // cnpj com formato válido, mas que a consulta à API não encontrou
    public static ExcecaoDeJSP cnpjNaoEncontrado(){
        return new ExcecaoDeJSP("CNPJ inválido ou não encontrado.");
    }

    // Exceções de not null
    public static ExcecaoDeJSP notNullVazio(String campo){
        String mensagem = "Por favor, preencha o campo %s.".formatted(rotulo(campo));
        return new ExcecaoDeJSP(mensagem);
    }

    // Constraint -> senha ou email diferentes do acesso ao banco de dados
    public static ExcecaoDeJSP falhaLogin() {
        return new ExcecaoDeJSP("E-mail ou senha inválidos.");
    }

    // inválido no regex
    public static ExcecaoDeJSP valorInvalido(String campo) {
        String msg = "O valor informado para %s é inválido.".formatted(rotulo(campo));
        return new ExcecaoDeJSP(msg);
    }

    // senha fora da regra
    public static ExcecaoDeJSP senhaFraca() {
        return new ExcecaoDeJSP(MENSAGEM_SENHA_FRACA);
    }

    public static final String MENSAGEM_SENHA_FRACA =
            "A senha deve ter no mínimo 8 caracteres, com pelo menos uma letra e um número.";

    // texto maior que o limite
    public static ExcecaoDeJSP tamanhoExcedido(String campo, int maximo) {
        String msg = "O campo %s deve ter no máximo %d caracteres.".formatted(rotulo(campo), maximo);
        return new ExcecaoDeJSP(msg);
    }

    // exclusão de super administrador
    public static ExcecaoDeJSP naoPodeExcluirPropriaConta() {
        return new ExcecaoDeJSP("Você não pode excluir a sua própria conta.");
    }

    public static ExcecaoDeJSP naoPodeExcluirUltimoAdmin() {
        return new ExcecaoDeJSP("Não é possível excluir o único administrador do sistema.");
    }

    // vencimento do contrato antes da data de início
    public static ExcecaoDeJSP vencimentoAntesDoInicio() {
        return new ExcecaoDeJSP("A data de vencimento não pode ser anterior à data de início do contrato.");
    }

    // data que não pode estar no futuro
    public static ExcecaoDeJSP dataNoFuturo(String campo) {
        String msg = "A data informada para %s não pode estar no futuro.".formatted(rotulo(campo));
        return new ExcecaoDeJSP(msg);
    }
}
