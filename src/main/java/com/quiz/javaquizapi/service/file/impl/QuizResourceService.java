package com.quiz.javaquizapi.service.file.impl;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.dao.ResourceRepository;
import com.quiz.javaquizapi.exception.file.ResourceNotFoundException;
import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceFolder;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.file.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

@Slf4j
@Service
public class QuizResourceService extends BaseQuizService<Resource> implements ResourceService {
    public QuizResourceService(BaseRepository<Resource> repository) {
        super(repository);
    }

    @Override
    public Resource get(String code) {
        logFetchingEntity();
        return getRepository().findByCode(code).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void create(Resource entity) {
        setCodeIfValid(entity);
        entity.setStatus(ResourceStatus.SAVED);
        log.info("Saving a resource...");
        getRepository().save(entity);
    }

    @Override
    public void update(Resource object) {
        log.info("Saving an updated resource...");
        getRepository().save(object);
    }

    @Override
    public Page<Resource> getApproved(Pageable pageable) {
        logFetchingByField("status");
        return cast(getRepository(), ResourceRepository.class).findAllApproved(pageable);
    }

    @Override
    public Page<Resource> getReadyForReview(Pageable pageable) {
        logFetchingByField("status");
        return cast(getRepository(), ResourceRepository.class).findAllReadyForReview(pageable);
    }

    @Override
    public Page<Resource> getByStatus(ResourceStatus status, Pageable pageable) {
        logFetchingByField("status");
        return cast(getRepository(), ResourceRepository.class).findByStatus(status, pageable);
    }

    @Override
    public Page<Resource> getByApprover(String approverCode, Pageable pageable) {
        logFetchingByField("approver");
        return cast(getRepository(), ResourceRepository.class).findByApprovedByCode(approverCode, pageable);
    }

    @Override
    public Page<Resource> getByCreator(String cratorCode, Pageable pageable) {
        logFetchingByField("creator");
        return cast(getRepository(), ResourceRepository.class).findByCreatedByCode(cratorCode, pageable);
    }

    @Override
    public Page<Resource> getByPath(String path, Pageable pageable) {
        logFetchingByField("folder path");
        return cast(getRepository(), ResourceRepository.class).findByFolderPath(path, pageable);
    }

    @Override
    public boolean delete(String code) {
        log.info("Deleting a resource...");
        var count = cast(getRepository(), ResourceRepository.class).deleteByCode(code);
        return count == 1;
    }

    @Override
    public boolean deleteBatch(Page<Resource> resources) {
        log.info("Deleting a batch of resources...");
        try {
            getRepository().deleteAll(resources.toList());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
