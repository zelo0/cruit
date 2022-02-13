package com.project.cruit.exception;

import com.project.cruit.dto.SimpleMessageBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<SimpleMessageBody> handleRegisterException(Exception e) {
        return ResponseEntity.badRequest().body(new SimpleMessageBody(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SimpleMessageBody> handleMethodArgumentNotValidException(Exception e) {
        return ResponseEntity.badRequest().body(new SimpleMessageBody("필드의 경고를 확인 후 수정해주세요"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<SimpleMessageBody> handleRuntimeException(Exception e) {
        return ResponseEntity.badRequest().body(new SimpleMessageBody("예기치 못한 분제가 발생했습니다"));
    }
}
