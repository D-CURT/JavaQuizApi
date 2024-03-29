package com.quiz.javaquizapi.service.mail.impl;

import com.quiz.javaquizapi.exception.mail.QuizMailException;
import com.quiz.javaquizapi.service.mail.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class QuizMailSenderService implements MailSenderService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    public void send(SimpleMailMessage message) {
        message.setFrom(fromMail);
        try {
            log.info("Sending an email...");
            mailSender.send(message);
        } catch (MailException e) {
            throw new QuizMailException(e.getMessage());
        }
        log.info("The email has been sent successfully.");
    }
}
