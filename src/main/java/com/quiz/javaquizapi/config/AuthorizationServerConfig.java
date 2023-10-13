package com.quiz.javaquizapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class AuthorizationServerConfig {

    private final PasswordEncoder passwordEncoder;
    private final AuthorizationServerProperties authorizationServerProperties;

    @Value("${spring.security.oauth2.token.access.time-to-live-in-min}")
    private long accessTokenTimeToLive;

    @Value("${spring.security.oauth2.token.refresh.time-to-live-in-hour}")
    private long refreshTokenTimeToLive;

    @Value("${spring.security.oauth2.token.code.time-to-live-in-min}")
    private long authCodeTimeToLive;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.exceptionHandling(config -> config
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new InMemoryRegisteredClientRepository(
                RegisteredClient
                        .withId("quiz-client-id")
                        .clientId("quiz-client-id")
                        .clientName("Quiz client")
                        .clientSecret(passwordEncoder.encode("quiz-client-secret"))
                        .redirectUri("http://localhost:8080/code")
                        .scope("read.scope")
                        .scope("write.scope")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                                .accessTokenTimeToLive(Duration.of(accessTokenTimeToLive, ChronoUnit.MINUTES))
                                .refreshTokenTimeToLive(Duration.of(refreshTokenTimeToLive, ChronoUnit.HOURS))
                                .reuseRefreshTokens(Boolean.FALSE)
                                .authorizationCodeTimeToLive(Duration.of(authCodeTimeToLive, ChronoUnit.MINUTES))
                                .build())
                        .build()
        );
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(authorizationServerProperties.getIssuerUrl())
                .tokenIntrospectionEndpoint(authorizationServerProperties.getIntrospectionEndpoint())
                .build();
    }
}
