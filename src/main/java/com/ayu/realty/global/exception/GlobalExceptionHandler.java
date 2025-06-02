package com.ayu.realty.global.exception;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.ErrorType.ErrorType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 커스텀 예외 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiRes.fail(e.getErrorCode()));
    }

    // 2. Enum 변환 에러 (Role 등)
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> handleInvalidFormat(InvalidFormatException e) {
        if (e.getTargetType().isEnum() && e.getTargetType().getSimpleName().equals("Role")) {
            return ResponseEntity
                    .status(ErrorCode.INVALID_ROLE.getStatus())
                    .body(ApiRes.fail(ErrorCode.INVALID_ROLE));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiRes.fail(new ErrorType() {
                    @Override
                    public String getCode() {
                        return "E400";
                    }

                    @Override
                    public String getMessage() {
                        return "입력값이 잘못되었습니다.";
                    }

                    @Override
                    public int getStatus() {
                        return HttpStatus.BAD_REQUEST.value();
                    }
                }));
    }

    // 3. 회원 없음 예외
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.MEMBER_NOT_FOUND.getStatus())
                .body(ApiRes.fail(ErrorCode.MEMBER_NOT_FOUND));
    }

    //4. 회원 권한 없음
    @ExceptionHandler(AccessDeniedException.class)
    public  ResponseEntity<?> handleAccessDenied(AccessDeniedException e) {
        log.warn("접근거부: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.ACCESS_DENIED.getStatus())
                .body(ApiRes.fail(ErrorCode.ACCESS_DENIED));
    }

    //5. 비밀번호 일치하지 않음
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> handleInvalidPassword(InvalidPasswordException e) {
        log.warn("비밀번호 불일치 {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INVALID_PASSWORD.getStatus())
                .body(ErrorCode.INVALID_PASSWORD);
    }


    @ExceptionHandler(MissingServletRequestPartException.class)
    public void handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        log.error(">>> Multipart 파일 누락: {}", ex.getMessage());
    }

}