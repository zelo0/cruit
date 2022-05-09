package com.project.cruit.exception;

public class NotPermitException extends RuntimeException {
    public NotPermitException(String message) {
        super("권한이 없습니다. " + message);
    }
}
