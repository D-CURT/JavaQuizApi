package com.quiz.javaquizapi.config;

import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.quiz.javaquizapi.service.box.FileClient;
import com.quiz.javaquizapi.service.box.impl.QuizBoxClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@Configuration
public class BoxConfig {
    public static final String CONFIG_PATH = "src/main/resources/box/config.json";

    @Value("${box.client.client-id}")
    private String clientId;

    @Value("${box.client.client-secret}")
    private String clientSecret;

    @Value("${box.client.enterprise-id}")
    private String enterpriseId;

    @Value("${box.client.user-id}")
    private String userId;

    @Bean
    public FileClient boxClient() throws IOException {
        com.box.sdk.BoxConfig config;
        try (Reader reader = new FileReader(CONFIG_PATH)) {
            config = com.box.sdk.BoxConfig.readFrom(reader);
        }
        var api = BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(config);
        return QuizBoxClient.of(api).init();
    }
}
