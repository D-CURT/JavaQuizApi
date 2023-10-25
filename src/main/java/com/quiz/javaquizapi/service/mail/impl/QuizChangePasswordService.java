package com.quiz.javaquizapi.service.mail.impl;

import com.quiz.javaquizapi.exception.user.PasswordCodeExpiredException;
import com.quiz.javaquizapi.model.user.PasswordCode;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.mail.ChangePasswordService;
import com.quiz.javaquizapi.service.mail.MailSenderService;
import com.quiz.javaquizapi.service.me.user.PasswordCodeService;
import com.quiz.javaquizapi.service.me.user.UserService;
import com.quiz.javaquizapi.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizChangePasswordService implements ChangePasswordService {
    /**
     * This constant provides an email subject.
     * <p>The property key is - '<strong>api.errorCode.121</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int CHANGE_PASS_SUBJECT = 121;
    /**
     * This constant provides an email body text.
     * <p>The property key is - '<strong>api.errorCode.122</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int CHANGE_PASS_BODY = 122;
    public static final int CODE_LENGTH = 4;

    private final PasswordCodeService codeService;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSender;
    private final UserService userService;
    private final MessageSource msgSource;

    @Override
    public void sendCode(String toEmail) {
        log.info("Preparing an email message to send...");
        var message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(ResponseService.getMessage(msgSource, CHANGE_PASS_SUBJECT));
        String code = RandomStringUtils.random(CODE_LENGTH, false, true);
        message.setText(ResponseService.getMessage(msgSource, CHANGE_PASS_BODY, code));
        mailSender.send(message);
        codeService.create(
                new PasswordCode()
                        .setPasswordCode(code)
                        .setUser(userService.getMe(toEmail)));
    }

    @Override
    public void changePassword(PasswordCode code) {
        if (codeService.isValid(code)) {
            throw new PasswordCodeExpiredException();
        }
        User user = userService.getMe(code.getUser().getUsername());
        user.setPassword(passwordEncoder.encode(code.getPassword()));
        log.info("Saving a new password...");
        userService.update(user);
    }
}
