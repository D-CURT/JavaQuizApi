package com.quiz.javaquizapi.service.mail;

import org.springframework.mail.SimpleMailMessage;

/**
 * Provides ability to send email messages.
 */
public interface MailSenderService {
    void send(SimpleMailMessage message);
}
