package com.quiz.javaquizapi.service;

import com.quiz.javaquizapi.model.http.Response;

/**
 * Provides functionality to create the Quiz API {@link Response}.
 */
public interface ResponseService {

    /**
     * This constant contains a prefix to build an error message key.
     */
    String API_ERROR_CODE_PREFIX = "api.errorCode.";

    /**
     * Creates a positive response.
     *
     * @param data data of a positive response.
     * @return created Response.
     */
    Response build(Object data);

    /**
     * Creates an error response.
     *
     * @param data object that processing failed.
     * @param group error group.
     * @param messageKey message key to find a user-friendly description.
     * @param args arguments of a user-friendly description.
     *
     * @return created Response.
     */
    Response buildError(Object data, String group, String messageKey, String... args);

    /**
     * Creates an error response.
     *
     * @param data object that processing failed.
     * @param group error group.
     * @param errorCode integer code to build a user-friendly description message key.
     * @param args arguments of a user-friendly description.
     *
     * @return created Response.
     */
    Response buildError(Object data, String group, Integer errorCode, String... args);
}
