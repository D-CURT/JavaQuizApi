package com.quiz.javaquizapi.unit.service.mail;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.exception.mail.QuizMailException;
import com.quiz.javaquizapi.service.mail.MailSenderService;
import com.quiz.javaquizapi.service.mail.impl.QuizMailSenderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mail sender service tests")
public class MailSenderServiceTests extends ApiTests {
    public static final String TEST_MAIL = "test@mail.com";
    @Mock
    private JavaMailSender mailSender;
    private MailSenderService service;

    @BeforeEach
    void setUp() {
        initLog();
        service = new QuizMailSenderService(mailSender);
        ReflectionTestUtils.setField(service, "fromMail", TEST_MAIL);
    }

    @Test
    @DisplayName("Send mail to username")
    public void testSendingMailGivenCurrentUsername() {
        service.send(new SimpleMailMessage());
        var messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        var actual = messageCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getFrom()).isEqualTo(TEST_MAIL);
        assertThat(captureLogs()).contains("Sending an email...", "The email has been sent successfully.");
    }

    @Test
    @DisplayName("Send mail with failure")
    public void testSendingEmailThenThrowMailException() {
        var message = new SimpleMailMessage();
        String error = "An email cannot be sent.";
        doThrow(new MailSendException(error)).when(mailSender).send(message);
        var exception = assertThrows(QuizMailException.class, () -> service.send(message));
        assertThat(exception).isNotNull();
        assertThat(exception.getReason()).isEqualTo(error);
        assertThat(exception.getCode()).isEqualTo(QuizMailException.MAIL_ERROR_CODE);
        assertThat(captureLogs()).contains("Sending an email...");
        verify(mailSender).send(message);
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}
