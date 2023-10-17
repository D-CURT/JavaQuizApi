package com.quiz.javaquizapi.service.mail;

import com.quiz.javaquizapi.exception.QuizException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class QuizChangePasswordService implements MailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public String sendCode(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Restore password code");
        String code = RandomStringUtils.random(4, false, true);
        message.setText("Your code: " + code);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new QuizException("Unable to send a change password email", e.getMessage());
        }
        return code;
    }
}
