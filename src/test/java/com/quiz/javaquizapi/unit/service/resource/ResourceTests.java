package com.quiz.javaquizapi.unit.service.resource;

import com.quiz.javaquizapi.integration.facade.me.ProfileTests;
import com.quiz.javaquizapi.model.file.Resource;
import com.quiz.javaquizapi.model.file.ResourceStatus;
import com.quiz.javaquizapi.service.Pagination;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.stream.IntStream;

public abstract class ResourceTests extends ProfileTests {
    public static final String ID = RandomStringUtils.random(10, false, true);

    protected Pageable requestPage() {
        return new Pagination<Resource>(){}.requestPage();
    }

    protected Page<Resource> generateTestResources(int quantity, ResourceStatus status) {
        return new Pagination<Resource>(){}.getPage(IntStream.range(0, quantity)
                .mapToObj(num ->
                        new Resource()
                                .setResourceId(ID)
                                .setStatus(status)
                                .setCreatedBy(getLocalProfile())
                                .setApprovedBy(ResourceStatus.APPROVED.equals(status)
                                        ? getLocalProfile()
                                        : null))
                .toList());
    }
}
