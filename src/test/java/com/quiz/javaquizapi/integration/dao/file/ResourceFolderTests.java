package com.quiz.javaquizapi.integration.dao.file;

import com.quiz.javaquizapi.model.file.ResourceFolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Resource folder DAO tests")
public class ResourceFolderTests extends ParentResourceTests {
    @Test
    @DisplayName("Fetch folder by its path")
    public void testFetchingResourceFolderGivenValidPath() {
        var path = "test to fetch";
        var folder = new ResourceFolder().setParent(getRoot()).setName(path);
        folder.setCode(UUID.randomUUID().toString());
        getRepository().save(folder);
        getRepository().findByPath(path)
                .ifPresentOrElse(item -> {
                        assertThat(item).isNotNull();
                        assertThat(item.getPath()).isEqualTo(path);
                }, () -> Assertions.fail("Folder not found"));
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch folders by parent code")
    public void testFetchingResourceFoldersGivenParentCode() {
        createResourceFolders(5, getRoot());
        getRepository().findByParentCode(getRoot().getCode(), Pageable.ofSize(10))
                .ifPresentOrElse(folders -> {
                    assertThat(folders).isNotNull();
                    assertThat(folders.get().count()).isEqualTo(5);
                    assertThat(folders.get().map(ResourceFolder::getParent).allMatch(getRoot()::equals)).isTrue();
                    }, () -> Assertions.fail("Folders not found"));
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Delete a resource folder by its path")
    public void testDeletingResourceFolderGivenValidPath() {
        var folder = new ResourceFolder().setParent(getRoot()).setName("test to delete");
        folder.setCode(UUID.randomUUID().toString());
        getRepository().save(folder);
        assertThat(getRepository().deleteByPath(folder.getPath())).isEqualTo(1);
        assertExecutedQueries();
    }

    private void createResourceFolders(int number, ResourceFolder parent) {
        IntStream.range(1, number).mapToObj(num -> initResource(num, parent)).forEach(getRepository()::save);
    }

    private ResourceFolder initResource(int num, ResourceFolder parent) {
        var folder = new ResourceFolder().setParent(parent).setName("folder" + ++num);
        folder.setCode(UUID.randomUUID().toString());
        return folder;
    }
}
