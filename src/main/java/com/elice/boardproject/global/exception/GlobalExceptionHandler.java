package com.elice.boardproject.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 전역 예외 처리기를 정의하는 클래스입니다.
 * 모든 컨트롤러에서 발생하는 예외를 처리하여 사용자에게 일관된 오류 메시지를 제공합니다.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 유효성 검사 예외를 처리하는 메서드입니다.
     * 유효성 검사 실패 시 발생한 첫 번째 오류 메시지를 로그에 기록하고, 에러 페이지로 전달합니다.
     *
     * @param ex 유효성 검사 예외 객체
     * @return 에러 페이지 경로
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed");

        log.error("Validation error: {}", errorMessage);

        return "global/error";
    }

    /**
     * 일반 예외를 처리하는 메서드입니다.
     * 예외가 발생하면 해당 예외 메시지를 로그에 기록하고, 에러 페이지로 전달합니다.
     *
     * @param ex 발생한 예외 객체
     * @return 에러 페이지 경로
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralExceptions(Exception ex) {

        log.error("An error occurred: {}", ex.getMessage(), ex);

        return "global/error";
    }
}
