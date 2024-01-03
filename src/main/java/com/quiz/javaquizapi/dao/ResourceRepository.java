package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Accumulates all functionality of <strong>the resource</strong> in the database.
 */
public interface ResourceRepository extends BaseRepository<Resource> {
    /**
     * Finds all resources with approved status.
     *
     * @param pageable page info.
     * @return resources page.
     */
    @Query("SELECT r FROM Resource r WHERE r.status = com.quiz.javaquizapi.model.file.ResourceStatus.APPROVED")
    Page<Resource> findAllApproved(Pageable pageable);

    /**
     * Finds all resources with ready for review status.
     *
     * @param pageable page info.
     * @return resources page.
     */
    @Query("SELECT r FROM Resource r WHERE r.status = com.quiz.javaquizapi.model.file.ResourceStatus.READY_FOR_REVIEW")
    Page<Resource> findAllReadyForReview(Pageable pageable);

    /**
     * Finds all resources by accepted status.
     *
     * @param status resource status.
     * @param pageable page info.
     * @return resources page.
     */
    Page<Resource> findByStatus(ResourceStatus status, Pageable pageable);

    /**
     * Finds all resources by accepted approver code.
     *
     * @param approverCode approver code.
     * @param pageable page info.
     * @return resources page.
     */
    Page<Resource> findByApprovedByCode(String approverCode, Pageable pageable);

    /**
     * Finds all resources by accepted creator code.
     *
     * @param creatorCode creator code.
     * @param pageable page info.
     * @return resources page.
     */
    Page<Resource> findByCreatedByCode(String creatorCode, Pageable pageable);

    /**
     * Finds all approved resources by accepted folder path.
     *
     * @param path folder path.
     * @param pageable page info.
     * @return resources page.
     */
    @Query("SELECT r FROM Resource r INNER JOIN r.folder f " +
            "WHERE f.path = ?1 " +
            "AND r.status = com.quiz.javaquizapi.model.file.ResourceStatus.APPROVED")
    Page<Resource> findByFolderPath(String path, Pageable pageable);

    /**
     * Deletes a resource by its code.
     *
     * @param code resource code.
     * @return deleted resources count.
     */
    Integer deleteByCode(String code);
}
