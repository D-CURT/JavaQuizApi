package com.quiz.javaquizapi.model.http;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Contains an error details.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Error(String code, String group, String message) {
}
