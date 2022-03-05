package com.project.cruit.exception;

public class NotPermitException extends RuntimeException {
    @Override
    public String getMessage() {
        return "권한이 없습니다";
    }
}
