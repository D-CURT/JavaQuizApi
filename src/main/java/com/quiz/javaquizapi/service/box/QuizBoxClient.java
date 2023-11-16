package com.quiz.javaquizapi.service.box;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxAPIResponseException;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.quiz.javaquizapi.exception.box.BoxApiException;
import com.quiz.javaquizapi.exception.box.BoxItemNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class QuizBoxClient implements BoxClient {
    public static final String ROOT_NAME = "Java Quiz API";
    public static final String[] BASIC_INFO_FIELDS = {"id", "name"};
    public static final int BOX_ITEM_ALREADY_EXIST_CODE = 409;

    private final BoxAPIConnection api;
    @Getter
    private BoxFolder root;

    public QuizBoxClient init() {
        try {
            root = findRoot().orElseGet(() -> BoxFolder.getRootFolder(api).createFolder(ROOT_NAME).getResource());
        } catch (BoxAPIResponseException e) {
            if (BOX_ITEM_ALREADY_EXIST_CODE != e.getResponseCode()) {
                throw new IllegalStateException("Unable to initiate the app root folder: " + e.getMessage());
            }
        }
        log.info("Box client initialized.");
        return this;
    }

    @Override
    public OutputStream download(String id) {
        var stream = new ByteArrayOutputStream();
        try {
            new BoxFile(api, id).download(stream);
        } catch (Exception e) {
            throw new BoxApiException(e.getMessage());
        }
        return stream;
    }

    @Override
    public BoxItem.Info upload(MultipartFile file) {
        try (var stream = file.getInputStream()) {
            return findRoot()
                    .orElseThrow(() -> new BoxItemNotFoundException(file.getName()))
                    .uploadFile(stream, file.getName());
        } catch (Exception e) {
            throw new BoxApiException(e.getMessage());
        }
    }

    private Optional<BoxFolder> findRoot() {
        return StreamSupport.stream(BoxFolder.getRootFolder(api)
                        .getChildren(BASIC_INFO_FIELDS).spliterator(), false)
                .filter(info -> ROOT_NAME.equalsIgnoreCase(info.getName()))
                .map(BoxItem.Info::getResource)
                .map(folder -> cast(folder, BoxFolder.class))
                .findFirst();
    }
}
