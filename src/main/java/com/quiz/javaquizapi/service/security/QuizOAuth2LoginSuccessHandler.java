package com.quiz.javaquizapi.service.security;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.model.user.Providers;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

/**
 * Provides functionality to integrate a user authorized by an external provider into the Quiz API.
 */
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
                            user -> log.debug("Username '{}' already exists", user.getUsername()),
                            () -> {
                                User user = new User()
                                        .setUsername(oidcUser.getEmail())
                                        .setDisplayName(oidcUser.getAttribute("name"))
                                        .setRole(Roles.USER)
                                        .setEnabled(Boolean.TRUE)
                                        .setProvider(Providers.GOOGLE);
                                user.setCode(UUID.randomUUID().toString());
                                userRepository.save(user);
                                log.info("A new user created");
                                log.debug("Username '{}' resolved", oidcUser.getEmail());
                            });
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
