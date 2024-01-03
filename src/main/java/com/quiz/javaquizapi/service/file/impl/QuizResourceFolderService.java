package com.quiz.javaquizapi.service.file.impl;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.dao.ResourceFolderRepository;
import com.quiz.javaquizapi.exception.file.ResourceFolderNotFoundException;
import com.quiz.javaquizapi.model.file.ResourceFolder;
import com.quiz.javaquizapi.service.BaseUpdatableService;
import com.quiz.javaquizapi.service.file.ResourceFolderService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

@Slf4j
@Service
public class QuizResourceFolderService extends BaseUpdatableService<ResourceFolder> implements ResourceFolderService {
    public static final String ROOT_NAME = "root";

    public String rootCode;

    public QuizResourceFolderService(BaseRepository<ResourceFolder> repository) {
        super(repository);
    }

    @PostConstruct
    public void init() {
        rootCode = UUID.randomUUID().toString();
        var folder = new ResourceFolder().setName(ROOT_NAME);
        folder.setCode(rootCode);
        create(folder);
    }

    @Override
    public ResourceFolder get(String code) {
        logFetchingEntity();
        return getRepository().findByCode(code).orElseThrow(ResourceFolderNotFoundException::new);
    }

    @Override
    public ResourceFolder getByPath(String path) {
        logFetchingByField("path");
        return cast(getRepository(), ResourceFolderRepository.class)
                .findByPath(path)
                .orElseThrow(ResourceFolderNotFoundException::new);
    }

    @Override
    public void create(ResourceFolder entity) {
        setCodeIfValid(entity);
        log.info("Saving a resource folder...");
        getRepository().save(entity);
    }

    @Override
    public void update(ResourceFolder object) {
        log.info("Saving an updated resource folder...");
        getRepository().save(object);
    }

    @Override
    public Page<ResourceFolder> getByParent(String parentCode, Pageable pageable) {
        logFetchingByField("parent");
        return cast(getRepository(), ResourceFolderRepository.class)
                .findByParentCode(parentCode, pageable)
                .orElse(Page.empty());
    }

    @Override
    public boolean delete(String path) {
        log.info("Deleting a resource folder by its path...");
        return cast(getRepository(), ResourceFolderRepository.class).deleteByPath(path) == 1;
    }

    @Override
    public String getRoot() {
        return rootCode;
    }
}
