package com.quiz.javaquizapi.service.file;

import com.quiz.javaquizapi.model.file.ResourceFolder;
import com.quiz.javaquizapi.service.Pagination;
import com.quiz.javaquizapi.service.QuizService;
import com.quiz.javaquizapi.service.Updatable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Provides functionality to operate with a {@link com.quiz.javaquizapi.model.file.ResourceFolder}.
 */
public interface ResourceFolderService
        extends QuizService<ResourceFolder>, Updatable<ResourceFolder>, Pagination<ResourceFolder> {
    ResourceFolder getByPath(String path);

    Page<ResourceFolder> getByParent(String parentCode, Pageable pageable);

    boolean delete(String path);

    String getRoot();
}
