package com.customerproduct.customerproductapi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String errorMessage;
    private String developerMessage;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String errorMessage, String developerMessage) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .errorMessage(errorMessage)
                .developerMessage(developerMessage)
                .build();
    }
}
