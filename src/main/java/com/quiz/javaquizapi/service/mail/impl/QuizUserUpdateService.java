package com.quiz.javaquizapi.service.mail.impl;

import com.quiz.javaquizapi.exception.user.UserUpdateCodeExpiredException;
import com.quiz.javaquizapi.exception.user.UserFieldSameValueException;
import com.quiz.javaquizapi.model.user.UserUpdateCode;
import com.quiz.javaquizapi.service.mail.MailSenderService;
import com.quiz.javaquizapi.service.mail.UserUpdateService;
import com.quiz.javaquizapi.service.me.user.UserService;
import com.quiz.javaquizapi.service.me.user.UserUpdateCodeService;
import com.quiz.javaquizapi.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizUserUpdateService implements UserUpdateService {
    /**
     * This constant provides an email subject.
     * <p>The property key is - '<strong>api.errorCode.121</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int CHANGE_USER_SUBJECT = 121;
    /**
     * This constant provides an email body text.
     * <p>The property key is - '<strong>api.errorCode.122</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int CHANGE_USER_BODY = 122;
    public static final int CODE_LENGTH = 4;

    private final UserUpdateCodeService codeService;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSender;
    private final UserService userService;
    private final MessageSource msgSource;

    @Override
    public void sendCode(String toEmail) {
        log.info("Preparing an email message to send...");
        var message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(ResponseService.getMessage(msgSource, CHANGE_USER_SUBJECT));
        String code = RandomStringUtils.random(CODE_LENGTH, false, true);
        message.setText(ResponseService.getMessage(msgSource, CHANGE_USER_BODY, code));
        mailSender.send(message);
        codeService.create(
                new UserUpdateCode()
                        .setCheckNumber(code)
                        .setUser(userService.getMe(toEmail)));
    }

    @Override
    public void updateUser(UserUpdateCode code) {
        if (!codeService.isValid(code)) {
            throw new UserUpdateCodeExpiredException();
        }
        var user = userService.getMe(code.getUser().getUsername());
        log.info("Check if this user field value is already in use...");
        var updateType = code.getType();
        var currValue = updateType.getGetter().apply(user);
        var newValue = code.getValue();
        if (StringUtils.equals(currValue, newValue)) {
            throw new UserFieldSameValueException();
        }
        if (updateType.isPassword()) {
            newValue = passwordEncoder.encode(newValue);
        }
        updateType.set(newValue, user);
        log.info("Saving a new {} value...", updateType);
        code.setUser(user);
        userService.update(user);
        codeService.update(code);
    }
}
