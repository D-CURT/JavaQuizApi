package com.quiz.javaquizapi.service.box;

import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

/**
 * Provides all the operations with box files.
 */
public interface FileClient {
    OutputStream download(String id);

    String upload(MultipartFile file);

    void delete(String id);
}
