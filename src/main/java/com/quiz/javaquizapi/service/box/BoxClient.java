package com.quiz.javaquizapi.service.box;

import com.box.sdk.BoxItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

/**
 * Provides all the operations with box files.
 */
public interface BoxClient {
    OutputStream download(String id);
    BoxItem.Info upload(MultipartFile file);
}
