package com.quiz.javaquizapi.service.security;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.model.user.Providers;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof DefaultOidcUser oidcUser) {
            userRepository.findByUsername(oidcUser.getEmail())
                    .ifPresentOrElse(
                            user -> log.info("Username '{}' already exists", user.getUsername()),
                            () -> {
                                userRepository.save(new User()
                                        .setUsername(oidcUser.getEmail())
                                        .setRole(Roles.USER)
                                        .setEnabled(Boolean.TRUE)
                                        .setProvider(Providers.GOOGLE));
                                log.info("A new user with username '{}' created", oidcUser.getEmail());
                            });
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
