package com.example.springboot.common.dto;

import com.example.springboot.common.enums.HttpStatus;
import lombok.Data;

@Data
public class ApiResponse<T> {

    private int code;
    private String status;
    private T data;
    private String message;

    public static <T> ApiResponse<T> createSuccess(T data) {
        return new ApiResponse<>(HttpStatus.OK.getCode(), HttpStatus.OK.getStatus(), data);
    }

    public static <T> ApiResponse<T> createError(int code, String status, String message) {
        return new ApiResponse<>(code, status, null, message);
    }

    private ApiResponse(int code, String status, T data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }

    private ApiResponse(int code, String status, T data, String message) {
        this.code = code;
        this.status = status;
        this.data = data;
        this.message = message;
    }

}
