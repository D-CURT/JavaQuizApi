package com.quiz.javaquizapi.integration.dao.file;

import com.quiz.javaquizapi.dao.ResourceFolderRepository;
import com.quiz.javaquizapi.integration.dao.MeDaoTests;
import com.quiz.javaquizapi.model.file.ResourceFolder;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

@Getter(AccessLevel.PROTECTED)
public abstract class ParentResourceTests extends MeDaoTests<ResourceFolderRepository> {
    private ResourceFolder root;
    private ResourceFolder firstLayer;
    private ResourceFolder secondLayer;

    @BeforeEach
    void setUp() {
        root = new ResourceFolder().setPath(ResourceFolder.ROOT_PATH);
        root.setCode(UUID.randomUUID().toString());
        getRepository().save(root);
        firstLayer = new ResourceFolder().setParent(root).setPath("folder1");
        firstLayer.setCode(UUID.randomUUID().toString());
        getRepository().save(firstLayer);
        secondLayer = new ResourceFolder().setParent(firstLayer).setPath("folder2");
        secondLayer.setCode(UUID.randomUUID().toString());
        getRepository().save(secondLayer);
    }
}
