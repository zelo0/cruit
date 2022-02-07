package com.project.cruit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ResponseWrapper<T> {
    private final T data;
}
