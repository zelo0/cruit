package com.project.cruit.exception;

public class NameExistsException extends RegisterException {
    public NameExistsException() {
        super("이미 존재하는 이름입니다");
    }
}
