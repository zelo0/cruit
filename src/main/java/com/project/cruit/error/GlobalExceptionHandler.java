package com.project.cruit.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPageOffsetException.class)
    public ErrorResponse handleInvalidPageOffsetException(InvalidPageOffsetException e){
        return new ErrorResponse(400, "페이지 범위를 벗어났습니다");
    }
}
