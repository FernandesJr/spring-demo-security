package com.fernandes.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine template; //Thymeleaf

    public void enviarPedidoConfirmacaoCadastro(String destino, String codigo) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        //Adicionando variáveis a página que será enviada
        Context context = new Context();
        context.setVariable("titulo", "Bem vindo a clínica Spring Security");
        context.setVariable("texto", "Precisamos que confirme seu cadastro.");
        context.setVariable("linkConfirmacao",
                "http://localhost:8080/u/confirmacao/cadastro?codigo=" + codigo);

        String html = template.process("email/confirmacao", context);

        helper.setTo(destino);
        helper.setFrom("nao-responder@clinica.com"); //O From é setado pelo o application.properties. Alguns servidores iram mostrar esse email como uma espécie de máscara
        helper.setSubject("Confirmação de cadastro");
        helper.setText(html, true);

        //Lembrete sempre colocar as img por último
        helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png")); //importante! projeto KAIZEN

        mailSender.send(message);

    }

    public void pedidoDeRecuperacaoSenha(String destino, String verificador) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        //Adicionando variáveis a página que será enviada
        Context context = new Context();
        context.setVariable("titulo", "Recuperação de senha");
        context.setVariable("texto", "Segue o código verificador, informe-o na página de recuperação de senha.");
        context.setVariable("verificador", verificador);


        String html = template.process("email/confirmacao", context);

        helper.setTo(destino);
        helper.setFrom("nao-responder@clinica.com"); //O From é setado pelo o application.properties. Alguns servidores iram mostrar esse email como uma espécie de máscara
        helper.setSubject("Recuperação de senha");
        helper.setText(html, true);

        //Lembrete sempre colocar as img por último
        helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png")); //importante! projeto KAIZEN

        mailSender.send(message);

    }

}
