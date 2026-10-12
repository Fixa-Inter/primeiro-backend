package com.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EnviadorEmail {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    private final TemplateEmail templateEmail = new TemplateEmail();

    public void enviarCodigo(String destinatario, String nomeInstituicao, String codigo) {

        String remetente = dotenv.get("EMAIL_USER");
        String senha = dotenv.get("EMAIL_SENHA");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");                // vai precisa fazer login de usuario e senha
        props.put("mail.smtp.starttls.enable", "true");     // liga cliptografia TLS para a senha e nome nao serem enviadas pelo server de forma exposta
        props.put("mail.smtp.host", "smtp.gmail.com");      // sai do gmail
        props.put("mail.smtp.port", "587");                 // a porta de envio do TLS

        Session session = Session.getInstance(props, new Authenticator() {      // ambiente do email
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senha);
            }
        });

        String html = templateEmail.renderizarCodigo(nomeInstituicao, codigo, "5 minutos");

        try {
            Message mensagem = new MimeMessage(session);
            mensagem.setFrom(new InternetAddress(remetente));
            mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensagem.setContent(html, "text/html; charset=UTF-8");

            Transport.send(mensagem);

        } catch (AddressException e) {
            System.out.print("Erro no endereco para enviar a senha no cadastro de instituicao: " + e.getMessage());
        } catch (MessagingException e) {
            System.out.print("Erro ao configurar mensagem para enviar a senha no cadastro de instituicao: " + e.getMessage());
        }

    }

}
