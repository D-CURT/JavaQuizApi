package com.quiz.javaquizapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Provides a service page params.
 */
public interface Pagination<T> {
    int DEFAULT_PAGE_SIZE = 100;

    default int getPageSize() {
        return DEFAULT_PAGE_SIZE;
    }

    default Pageable requestPage() {
        return requestPage(0);
    }

    default Pageable requestPage(int pageNumber) {
        return Pageable.ofSize(getPageSize()).withPage(pageNumber);
    }

    default Page<T> getPage(List<T> elements) {
        return getPage(elements, 0);
    }

    default Page<T> getPage(List<T> elements, int pageNumber) {
        return new PageImpl<>(
                elements,
                pageNumber == 0 ? Pageable.unpaged() : requestPage(pageNumber),
                elements == null ? 0L : elements.size());
    }
}
