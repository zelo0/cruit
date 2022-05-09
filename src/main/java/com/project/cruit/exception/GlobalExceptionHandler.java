package com.project.cruit.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.project.cruit.dto.SimpleMessageBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<SimpleMessageBody> handleRegisterException(Exception e) {
        log.error(e.toString());
        return ResponseEntity.badRequest().body(new SimpleMessageBody("회원가입 중 예외가 발생했습니다"));
    }

    @ExceptionHandler(NotHaveSessionException.class)
    public ResponseEntity<SimpleMessageBody> handleSessionException(Exception e) {
        log.error(e.toString());
        return new ResponseEntity(new SimpleMessageBody("로그인돼있지 않습니다"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SimpleMessageBody> handleMethodArgumentNotValidException(Exception e) {
        log.error(e.toString());
        return ResponseEntity.badRequest().body(new SimpleMessageBody("필드의 경고를 확인 후 수정해주세요"));
    }


    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<SimpleMessageBody> handleMismatchedInputException(Exception e) {
        log.error(e.toString());
        return ResponseEntity.badRequest().body(new SimpleMessageBody("전달된 데이터가 올바르지 않습니다"));
    }

    @ExceptionHandler(NotPermitException.class)
    public ResponseEntity<SimpleMessageBody> handleNotPermitException(Exception e) {
        log.error(e.toString());
        return new ResponseEntity(new SimpleMessageBody(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    // optional 객체에 들은 게 없을 때 get을 호출할 때, ....
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<SimpleMessageBody> handleNoSuchElementExceptionException(Exception e) {
        log.error(e.toString());
        return ResponseEntity.badRequest().body(new SimpleMessageBody("존재하지 않는 값을 요청했습니다"));
    }
    
    // 이미 닫힌 파트의 제안을 승낙할 시 예외 처리
    @ExceptionHandler(AlreadyClosedPartException.class)
    public ResponseEntity<SimpleMessageBody> handleAlreadyClosedPartException(Exception e){
        log.error(e.toString());
        return ResponseEntity.badRequest().body(new SimpleMessageBody("이미 닫힌 파트입니다"));
    }

    // 유효하지 않은 페이지 검색 시
    @ExceptionHandler(InvalidPageOffsetException.class)
    public ResponseEntity<SimpleMessageBody> handleInvalidPageOffsetException(Exception e){
        log.error(e.toString());
        return ResponseEntity.badRequest().body(new SimpleMessageBody("유효하지 않은 페이지이입니다"));
    }
    
    @ExceptionHandler(IOException.class)
    public ResponseEntity<SimpleMessageBody> handleIOException(Exception e){
        log.error(e.toString());
        return ResponseEntity.badRequest().body(new SimpleMessageBody("io 에러가 발생했습니다"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<SimpleMessageBody> handleRuntimeException(Exception e) {
        log.error(e.toString());
        return ResponseEntity.badRequest().body(new SimpleMessageBody("예기치 못한 분제가 발생했습니다"));
    }
}
