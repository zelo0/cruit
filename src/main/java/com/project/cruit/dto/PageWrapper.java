package com.project.cruit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PageWrapper<T> {
    private final T data;
    private final Boolean hasPrevious;
    private final Boolean hasNext;
    private final Integer totalPage;
    private final Integer currentPage;
}
