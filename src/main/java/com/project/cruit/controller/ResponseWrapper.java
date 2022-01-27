package com.project.cruit.controller;

import lombok.Data;

@Data
public class ResponseWrapper<T> {
    private final T data;
}
