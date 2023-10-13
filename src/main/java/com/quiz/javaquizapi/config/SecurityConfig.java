package com.quiz.javaquizapi.config;

import com.quiz.javaquizapi.service.security.QuizAuthenticationFailureHandler;
import com.quiz.javaquizapi.service.security.QuizOAuth2LoginSuccessHandler;
import com.quiz.javaquizapi.service.security.QuizUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final QuizAuthenticationFailureHandler failureHandler;
    private final QuizOAuth2LoginSuccessHandler successHandler;
    private final QuizUserDetailsService userDetailsService;
    private final OidcUserService userService = new OidcUserService();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(config -> config
                        .anyRequest().authenticated())
                .formLogin(config -> config.failureHandler(failureHandler))
                .oauth2Login(config -> config
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .userInfoEndpoint(userInfoConfig -> userInfoConfig.oidcUserService(oidcUserService())));
        http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .userDetailsService(userDetailsService);
        return http.build();
//                .logout(config -> config
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login.html"))
//                .oauth2Login(config -> config
//                        .userInfoEndpoint(
//                                userInfoEndpointConfig -> userInfoEndpointConfig.oidcUserService(oidcUserService())));
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return (userRequest) -> {
            OidcUser user = userService.loadUser(userRequest);
            return new DefaultOidcUser(user.getAuthorities(), user.getIdToken(), user.getUserInfo());
        };
    }
}
