package com.project.cruit.exception;

public class InvalidPageOffsetException extends RuntimeException {
    public InvalidPageOffsetException() {
        super("유효한 페이지 범위를 벗어났습니다.");
    }
}
