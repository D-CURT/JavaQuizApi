package com.quiz.javaquizapi.config;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.service.security.QuizAuthenticationEntryPoint;
import com.quiz.javaquizapi.service.security.QuizAuthenticationFailureHandler;
import com.quiz.javaquizapi.service.security.QuizUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final QuizAuthenticationFailureHandler failureHandler;
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
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationEventPublisher(new DefaultAuthenticationEventPublisher(eventPublisher));
        return builder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder())
                .and()
                .authenticationProvider(authenticationProvider());
        http.authorizeRequests()
                .antMatchers("/", "/user/authorization").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .failureHandler(failureHandler)
                .defaultSuccessUrl("/user")
                .and()
                .oauth2Login()
                .userInfoEndpoint().oidcUserService(oidcUserService())
                .and()
                .defaultSuccessUrl("/user")
                .and()
                .authenticationManager(authenticationManager(managerBuilder))
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and().csrf().disable();
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
