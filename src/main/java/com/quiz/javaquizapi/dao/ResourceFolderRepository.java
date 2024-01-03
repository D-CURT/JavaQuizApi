package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.file.ResourceFolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Accumulates all functionality of <strong>the resource folder</strong> in the database.
 */
public interface ResourceFolderRepository extends BaseRepository<ResourceFolder> {
    /**
     * Fetches folder by its path.
     *
     * @param path folder path.
     * @return optional folder.
     */
    Optional<ResourceFolder> findByPath(String path);

    /**
     * Fetches folders by parent code.
     *
     * @param parentCode parent code.
     * @param pageable page details.
     * @return page of folders.
     */
    Optional<Page<ResourceFolder>> findByParentCode(String parentCode, Pageable pageable);

    /**
     * Deletes a resource by its folder path.
     *
     * @param path folder path.
     * @return deleted resources count.
     */
    Integer deleteByPath(String path);
}
