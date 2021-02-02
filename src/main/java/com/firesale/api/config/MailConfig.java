package com.firesale.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    public static final String FROM = System.getenv("FIRESALE_MAILSERVER_FROM");

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(System.getenv("FIRESALE_MAILSERVER_HOST"));
        javaMailSender.setPort(587);
        javaMailSender.setUsername(System.getenv("FIRESALE_MAILSERVER_USER"));
        javaMailSender.setPassword(System.getenv("FIRESALE_MAILSERVER_PASS"));

        Properties javaMailProperties = javaMailSender.getJavaMailProperties();
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.debug", "true");

        return javaMailSender;
    }
}
