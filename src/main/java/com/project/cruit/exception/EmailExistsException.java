package com.project.cruit.exception;

public class EmailExistsException extends RegisterException {
    public EmailExistsException() {
        super("이미 존재하는 이메일입니다");
    }
}
