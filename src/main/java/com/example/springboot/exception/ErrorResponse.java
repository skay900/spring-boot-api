package com.example.springboot.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Data
public class ErrorResponse {
    private HttpStatusCode statusCode;
    private HttpStatus status;
    private String message;
    private String details;

    public ErrorResponse(HttpStatusCode statusCode, HttpStatus status, String message, String details) {
        super();
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.details = details;
    }

}