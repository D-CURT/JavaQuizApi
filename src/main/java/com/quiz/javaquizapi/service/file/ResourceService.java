package com.quiz.javaquizapi.service.file;

import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import com.quiz.javaquizapi.service.Pagination;
import com.quiz.javaquizapi.service.QuizService;
import com.quiz.javaquizapi.service.Updatable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Provides functionality to operate with a {@link Resource}.
 */
public interface ResourceService extends QuizService<Resource>, Updatable<Resource>, Pagination<Resource> {
    Page<Resource> getApproved(Pageable pageable);

    Page<Resource> getReadyForReview(Pageable pageable);

    Page<Resource> getByStatus(ResourceStatus status, Pageable pageable);

    Page<Resource> getByApprover(String approverCode, Pageable pageable);

    Page<Resource> getByCreator(String creatorCode, Pageable pageable);

    Page<Resource> getByPath(String path, Pageable pageable);

    boolean delete(String code);

    boolean deleteBatch(Page<Resource> resources);
}
