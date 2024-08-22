package com.example.springboot.exception.handler;

import com.example.springboot.common.dto.ApiResponse;
import com.example.springboot.exception.DuplicateEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleGlobalException(Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return createErrorResponse(httpStatus, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<Object> handleGlobalException(AccessDeniedException ex) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return createErrorResponse(httpStatus, ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ApiResponse<Object> handleGlobalException(DuplicateEmailException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return createErrorResponse(httpStatus, ex.getMessage());
    }

    private ApiResponse<Object> createErrorResponse(HttpStatus httpStatus, String message) {
        return ApiResponse.createError(httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }

}

