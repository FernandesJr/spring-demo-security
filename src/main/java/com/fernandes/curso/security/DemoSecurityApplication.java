package com.fernandes.curso.security;

import com.fernandes.curso.security.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoSecurityApplication implements CommandLineRunner {

	public static void main(String[] args) {
		//System.out.println(new BCryptPasswordEncoder().encode("123"));
		SpringApplication.run(DemoSecurityApplication.class, args);
	}

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private EmailService emailService;

	@Override
	public void run(String... args) throws Exception {
		/*SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("junior.fernandes@mcctecnologia.com.br");
		message.setSubject("Testando SpringBoot email");
		message.setText("Lindão deu certo");
		sender.send(message);
		System.out.println("Tentei enviar");*/

		emailService.enviarPedidoConfirmacaoCadastro("junior.fernandes@mcctecnologia.com.br","sad54f");
	}
}
