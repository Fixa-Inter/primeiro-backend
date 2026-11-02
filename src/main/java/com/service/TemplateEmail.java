package com.service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

// Renderiza os templates HTML de e-mail
public class TemplateEmail {

    // Motor do Thymeleaf que renderiza os templates
    private static final TemplateEngine engine = criarEngine();

    // Cria o motor do Thymeleaf que busca os templates
    private static TemplateEngine criarEngine() {

        // Define onde o Thymeleaf procura os templates

        ClassLoaderTemplateResolver resolver  = new ClassLoaderTemplateResolver();

        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");

        TemplateEngine e = new TemplateEngine();
        e.setTemplateResolver(resolver);
        return e;
    }

    // Monta o HTML do e-mail com o nome, o código e a validade
    public String renderizarCodigo(String nome, String codigo, String validade){

        Context context = new Context();
        context.setVariable("nome", nome);
        context.setVariable("codigo", codigo);
        context.setVariable("validade", validade);

        return engine.process("senhaCadastroInstituicao", context);
    }

}
