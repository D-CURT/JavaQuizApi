package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * Accumulates all functionality of <strong>the resource</strong> in the database.
 */
public interface ResourceRepository extends BaseRepository<Resource> {
    @Query("SELECT r FROM Resource r WHERE r.status = com.quiz.javaquizapi.model.file.ResourceStatus.APPROVED")
    Page<Resource> findAllApproved(Pageable pageable);
    @Query("SELECT r FROM Resource r WHERE r.status = com.quiz.javaquizapi.model.file.ResourceStatus.READY_FOR_REVIEW")
    Page<Resource> findAllReadyForReview(Pageable pageable);
    Page<Resource> findByStatus(ResourceStatus status, Pageable pageable);
    Page<Resource> findByApprovedByCode(String approverCode, Pageable pageable);
    Page<Resource> findByCreatedByCode(String creatorCode, Pageable pageable);
    Page<Resource> findByFolderPath(String path, Pageable pageable);
}
