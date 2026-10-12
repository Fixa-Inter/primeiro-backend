package com.service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class TemplateEmail {

    // base para o funcionamento do thymeleaf
    private static final TemplateEngine engine = criarEngine();

    private static TemplateEngine criarEngine() {

        //  configura o thymeleaf a onde e como achar o template

        ClassLoaderTemplateResolver resolver  = new ClassLoaderTemplateResolver();

        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");

        TemplateEngine e = new TemplateEngine();
        e.setTemplateResolver(resolver);
        return e;
    }

    public String renderizarCodigo(String nome, String codigo, String validade){

        Context context = new Context();
        context.setVariable("nome", nome);
        context.setVariable("codigo", codigo);
        context.setVariable("validade", validade);

        return engine.process("senhaCadastroInstituicao", context);
    }

}
