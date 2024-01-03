package com.quiz.javaquizapi.facade.resource;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.dto.resource.ResourceDto;
import com.quiz.javaquizapi.facade.BaseQuizFacade;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceFolder;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.service.Pagination;
import com.quiz.javaquizapi.service.box.FileClient;
import com.quiz.javaquizapi.service.file.ResourceFolderService;
import com.quiz.javaquizapi.service.file.ResourceService;
import com.quiz.javaquizapi.service.me.profile.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.function.Function;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

@Slf4j
@Facade
public class QuizResourceFacade extends BaseQuizFacade<Resource, ResourceDto> implements ResourceFacade {
    private final RecursiveRemover remover = new RecursiveRemover();
    private final ResourceFolderService folderService;
    private final ProfileService profileService;
    private final FileClient client;

    public QuizResourceFacade(ResourceService service, Mapper mapper, ResourceFolderService folderService, ProfileService profileService, FileClient client) {
        super(service, mapper);
        this.profileService = profileService;
        this.folderService = folderService;
        this.client = client;
    }

    @Override
    public void create(ResourceDto dto) {
        throw new UnsupportedOperationException("Invalid create call, use the correct signature");
    }

    @Override
    public ResourceDto create(ResourceDto data, MultipartFile file) {
        var parent = folderService.get(data.getFolderCode());
        var profile = profileService.getMe(data.getUsername());
        var entity = new Resource()
                .setResourceId(client.upload(file))
                .setCreatedBy(profile)
                .setFolder(parent);
        service().create(entity);
        mapper().map(entity, data);
        return data;
    }

    @Override
    public OutputStream download(String code) {
        return client.download(service().get(code).getResourceId());
    }

    @Override
    public void approve(ResourceDto data) {
        log.info("Approving a resource...");
        var profile = profileService.getMe(data.getUsername());
        updateResource(data.getCode(), profile, ResourceStatus.APPROVED);
    }

    @Override
    public void sendToReview(String resourceCode) {
        log.info("Sanding to review a resource...");
        updateResource(resourceCode, null, ResourceStatus.READY_FOR_REVIEW);
    }

    @Override
    public void review(String resourceCode) {
        log.info("Starting review of resource...");
        updateResource(resourceCode, null, ResourceStatus.IN_REVIEW);
    }

    @Override
    public Page<ResourceDto> getApproved(Pageable pageable) {
        return mapper()
                .mapPage(cast(service(), ResourceService.class).getApproved(pageable), dataType());
    }

    @Override
    public Page<ResourceDto> getReadyForReview(Pageable pageable) {
        return mapper()
                .mapPage(cast(service(), ResourceService.class).getReadyForReview(pageable), dataType());
    }

    @Override
    public Page<ResourceDto> getByStatus(ResourceStatus status, Pageable pageable) {
        return mapper()
                .mapPage(cast(service(), ResourceService.class).getByStatus(status, pageable), dataType());
    }

    @Override
    public Page<ResourceDto> getByApprover(String approverCode, Pageable pageable) {
        return mapper()
                .mapPage(cast(service(), ResourceService.class).getByApprover(approverCode, pageable), dataType());
    }

    @Override
    public Page<ResourceDto> getByCreator(String creatorCode, Pageable pageable) {
        return mapper()
                .mapPage(cast(service(), ResourceService.class).getByCreator(creatorCode, pageable), dataType());
    }

    @Override
    public Page<ResourceDto> getByPath(String path, Pageable pageable) {
        return mapper()
                .mapPage(cast(service(), ResourceService.class).getByPath(path, pageable), dataType());
    }

    @Override
    public void delete(String code) {
        cast(service(), ResourceService.class).delete(code);
    }

    @Override
    public void createFolder(String parentCode, String name) {
        var folder = folderService.get("root".equals(parentCode) ? folderService.getRoot() : parentCode);
        folderService.create(new ResourceFolder().setParent(folder).setName(name));
    }

    @Override
    public void renameFolder(String folderCode, String name) {
        var folder = folderService.get(folderCode);
        folderService.update(folder.setName(name));
    }

    @Override
    public void moveFolder(String code, String parentCode) {
        var folder = folderService.get(code);
        folderService.update(folder.setParent(folderService.get(parentCode)));
    }

    @Override
    public void deleteFolder(String path) {
        var folder = folderService.getByPath(path);
        remover.remove(folder);
    }

    private void updateResource(String resourceCode, Profile profile, ResourceStatus status) {
        var resource = service().get(resourceCode);
        if (ResourceStatus.APPROVED.equals(status)) {
            resource.setApprovedBy(profile);
        }
        cast(service(), ResourceService.class).update(resource.setStatus(status));
    }

    private class RecursiveRemover {
        public void remove(ResourceFolder folder) {
            log.info("Deleting all the resources within path '{}'...", folder.getPath());
            new ResourcePager(folder.getPath()).forEachRemaining(this::removeFiles);
            log.info("Deleting child folders recursively...");
            new ResourceFolderPager(folder.getCode()).forEachRemaining(this::removeFolders);
            folderService.delete(folder.getPath());
            log.info("Folder '{}' fully removed.", folder.getPath());
        }

        private void removeFiles(Page<Resource> files) {
            log.info("Removing files from the cloud...");
            files.get().map(Resource::getResourceId).forEach(client::delete);
            log.info("Removing resources form the DB...");
            cast(service(), ResourceService.class).deleteBatch(files);
        }

        private void removeFolders(Page<ResourceFolder> folders) {
            folders.get().forEach(this::remove);
        }
    }

    private class ResourcePager extends Pager<Resource> {
        private ResourcePager(String path) {
            super(pageable -> cast(service(), ResourceService.class).getByPath(path, pageable),
                    cast(service(), ResourceService.class));
        }
    }

    private class ResourceFolderPager extends Pager<ResourceFolder> {
        private ResourceFolderPager(String parentCode) {
            super(pageable -> folderService.getByParent(parentCode, pageable), folderService);
        }
    }

    private abstract static class Pager<T extends BaseEntity> implements Iterator<Page<T>> {
        private final Function<Pageable, Page<T>> producer;

        private Page<T> page;

        private <P extends Pagination<T>> Pager(Function<Pageable, Page<T>> producer, P pagination) {
            this.producer = producer;
            loadNextPage(pagination.requestPage());
        }

        @Override
        public boolean hasNext() {
            return (page.isFirst() && !page.isEmpty()) || page.hasNext();
        }

        @Override
        public Page<T> next() {
            var curr = page;
            if (page.hasNext()) {
                loadNextPage(page.nextPageable());
            }
            return curr;
        }
        private void loadNextPage(Pageable pageable) {
            page = producer.apply(pageable);
        }
    }
}
