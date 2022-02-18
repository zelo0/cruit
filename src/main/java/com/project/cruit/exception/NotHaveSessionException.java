package com.project.cruit.exception;

public class NotHaveSessionException extends RuntimeException {
    public NotHaveSessionException() {
        super("로그인이 필요합니다");
    }
}
