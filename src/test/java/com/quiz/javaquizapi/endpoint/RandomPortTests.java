package com.quiz.javaquizapi.endpoint;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.JavaQuizApiApplication;
import com.quiz.javaquizapi.facade.mapping.QuizModelMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SuppressWarnings("all")
@SpringBootTest(
        classes = {JavaQuizApiApplication.class, QuizModelMapper.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RandomPortTests extends ApiTests {

    public static final String LOCAL_URL = "http://localhost:%s/%s";
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    protected int port;

    protected String buildUrl(String uri) {
        return LOCAL_URL.formatted(port, StringUtils.removeStart(uri, "/"));
    }
}
