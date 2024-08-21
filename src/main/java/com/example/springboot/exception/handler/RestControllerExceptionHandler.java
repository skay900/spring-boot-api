package com.example.springboot.exception.handler;

import com.example.springboot.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

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
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return createErrorResponse(httpStatus, ex.getMessage());
    }

    private ApiResponse<Object> createErrorResponse(HttpStatus httpStatus, String message) {
        return ApiResponse.createError(httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }

}

