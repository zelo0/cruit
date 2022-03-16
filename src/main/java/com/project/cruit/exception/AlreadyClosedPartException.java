package com.project.cruit.exception;

public class AlreadyClosedPartException extends RuntimeException {
    public AlreadyClosedPartException() {
        super("이미 모집이 마감된 파트입니다");
    }
}
