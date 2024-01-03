package com.quiz.javaquizapi.facade.resource;

import com.quiz.javaquizapi.dto.resource.ResourceDto;
import com.quiz.javaquizapi.facade.QuizFacade;
import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

public interface ResourceFacade extends QuizFacade<Resource, ResourceDto> {
    OutputStream download(String code);

    void approve(ResourceDto data);

    void sendToReview(String resourceCode);

    void review(String resourceCode);

    ResourceDto create(ResourceDto data, MultipartFile file);

    Page<ResourceDto> getApproved(Pageable pageable);

    Page<ResourceDto> getReadyForReview(Pageable pageable);

    Page<ResourceDto> getByStatus(ResourceStatus status, Pageable pageable);

    Page<ResourceDto> getByApprover(String approverCode, Pageable pageable);

    Page<ResourceDto> getByCreator(String creatorCode, Pageable pageable);

    Page<ResourceDto> getByPath(String path, Pageable pageable);

    void delete(String code);

    void createFolder(String parentCode, String name);

    void renameFolder(String folderCode, String name);

    void moveFolder(String folderCode, String parentCode);

    void deleteFolder(String path);
}
