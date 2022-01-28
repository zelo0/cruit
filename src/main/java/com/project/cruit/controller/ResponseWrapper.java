package com.project.cruit.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseWrapper<T> {
    private final T data;
}
