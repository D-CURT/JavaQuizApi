package com.quiz.javaquizapi.exception.file;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a resource folder not found.
 */
public class ResourceFolderNotFoundException extends ResourceNotFoundException {
    /**
     * This constant provides an error code for the case when the resource folder not found.
     * <p>The property key is - '<strong>api.errorCode.211</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int RESOURCE_FOLDER_NOT_FOUND_CODE = 211;
    public static final String DEFAULT_ERROR = "Resource folder not found";

    public ResourceFolderNotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_ERROR, RESOURCE_FOLDER_NOT_FOUND_CODE);
    }
}
