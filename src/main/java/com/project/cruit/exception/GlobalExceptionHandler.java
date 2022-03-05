package com.project.cruit.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.project.cruit.dto.SimpleMessageBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<SimpleMessageBody> handleRegisterException(Exception e) {
        return ResponseEntity.badRequest().body(new SimpleMessageBody(e.getMessage()));
    }

    @ExceptionHandler(NotHaveSessionException.class)
    public ResponseEntity<SimpleMessageBody> handleSessionException(Exception e) {
        return new ResponseEntity(new SimpleMessageBody(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SimpleMessageBody> handleMethodArgumentNotValidException(Exception e) {
        return ResponseEntity.badRequest().body(new SimpleMessageBody("필드의 경고를 확인 후 수정해주세요"));
    }
    
    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<SimpleMessageBody> handleMismatchedInputException(Exception e) {
        return ResponseEntity.badRequest().body(new SimpleMessageBody("전달된 데이터가 올바르지 않습니다"));
    }

    @ExceptionHandler(NotPermitException.class)
    public ResponseEntity<SimpleMessageBody> handleNotPermitException(Exception e) {
        return new ResponseEntity(new SimpleMessageBody(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<SimpleMessageBody> handleRuntimeException(Exception e) {

        return ResponseEntity.badRequest().body(new SimpleMessageBody("예기치 못한 분제가 발생했습니다"));
    }
}
