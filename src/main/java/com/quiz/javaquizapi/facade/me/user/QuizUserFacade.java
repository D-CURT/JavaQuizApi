package com.quiz.javaquizapi.facade.me.user;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.dao.UserPassRepository;
import com.quiz.javaquizapi.dto.RestorePasswordDto;
import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.exception.QuizException;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.BaseMeFacade;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.model.user.UserPasswordCode;
import com.quiz.javaquizapi.service.mail.MailSenderService;
import com.quiz.javaquizapi.service.me.user.UserService;import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Provides intermediary operations related to a {@link com.quiz.javaquizapi.model.user.User}.
 */
@Slf4j
@Facade
public class QuizUserFacade extends BaseMeFacade<User, UserDto> implements UserFacade {

    private final MailSenderService mailSenderService;
    private final UserPassRepository userPassRepository;

    @Autowired
    public QuizUserFacade(
            UserService service,
            MailSenderService mailSenderService,
            UserPassRepository userPassRepository,
            Mapper mapper) {
        super(service, mapper);
        this.mailSenderService = mailSenderService;
        this.userPassRepository = userPassRepository;
    }

    @Override
    public void create(UserDto dto) {
        log.info("Starting a new user authorization...");
        User user = mapper.map(dto, User.class);
        service.create(user);
        mapper.map(user, dto);
        log.info("User authorization succeeded.");
    }

    @Override
    public void sendPasswordCode(String toEmail) {
        log.info("Sending the user password code...");
        String code = mailSenderService.sendCode(toEmail);
        UserPasswordCode userPasswordCode = new UserPasswordCode().setUsername(toEmail).setPassCode(code);
        userPasswordCode.setCode(UUID.randomUUID().toString());
        userPassRepository.save(userPasswordCode);
        log.info("Restore password code sent successfully.");
    }

    @Override
    public UserDto restorePassword(String username, RestorePasswordDto dto) {
        UserPasswordCode userPasswordCode = userPassRepository.findByUsername(username)
                .orElseThrow(() -> new QuizException("Password code not found for given username"));
        if (!dto.getCode().equals(userPasswordCode.getPassCode())) {
            throw new QuizException("Password code mismatch");
        }
        User user = service.getMe(username);
        user.setPassword(dto.getNewPass());
        ((UserService) service).update(user);
        return mapper.map(user, UserDto.class);
    }
}
