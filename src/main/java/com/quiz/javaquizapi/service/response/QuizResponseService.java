package com.quiz.javaquizapi.service.response;

import com.google.common.base.CaseFormat;
import com.quiz.javaquizapi.model.http.Error;
import com.quiz.javaquizapi.model.http.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Provides functionality to create the Quiz API {@link Response}.
 */
@Service
@RequiredArgsConstructor
public class QuizResponseService implements ResponseService {
    private final MessageSource msgSource;

    @Override
    public Response ok() {
        return build(null);
    }

    @Override
    public Response build(Object data) {
        return new Response().setData(data);
    }

    @Override
    public Response buildError(Object data, String group, Integer errorCode, String... args) {
        return buildError(data, group, API_ERROR_CODE_PREFIX + errorCode, args);
    }

    @Override
    public MessageSource source() {
        return msgSource;
    }

    @Override
    public Response buildError(Object data, String group, String key, String... args) {
        Response response = new Response().setData(data);
        Optional.ofNullable(key)
                .filter(StringUtils::isNotBlank)
                .map(error -> new Error(
                        key,
                        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, group),
                        ResponseService.getMessage(msgSource, key, args))
                ).ifPresent(response::setError);
        return response;
    }
}
