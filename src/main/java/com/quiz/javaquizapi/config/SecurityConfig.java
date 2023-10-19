package com.quiz.javaquizapi.config;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.service.security.QuizAuthenticationEntryPoint;
import com.quiz.javaquizapi.service.security.QuizAuthenticationFailureHandler;
import com.quiz.javaquizapi.service.security.QuizOAuth2LoginSuccessHandler;
import com.quiz.javaquizapi.service.security.QuizUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    public static final String[] AUTH_WHITELIST = {
            "/",
            "/user/authorization"
    };

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final QuizAuthenticationFailureHandler failureHandler;
    private final QuizOAuth2LoginSuccessHandler successHandler;
    private final QuizAuthenticationEntryPoint entryPoint;

    @Bean
    public UserDetailsService userDetailsService() {
        return new QuizUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(config -> config
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(entryPoint))
                .formLogin(config -> config
                        .loginPage("/login").permitAll()
                        .failureHandler(failureHandler)
                        .defaultSuccessUrl("/user/me"))
                .oauth2Login(config -> config
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .defaultSuccessUrl("/user/me")
                        .userInfoEndpoint(userInfoConfig -> userInfoConfig.oidcUserService(oidcUserService())));
        http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationEventPublisher(new DefaultAuthenticationEventPublisher(eventPublisher))
                .authenticationProvider(authenticationProvider())
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
        return http.build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService service = new OidcUserService();
        return (userRequest) -> {
            OidcUser user = service.loadUser(userRequest);
            return new DefaultOidcUser(user.getAuthorities(), user.getIdToken(), user.getUserInfo());
        };
    }
}
