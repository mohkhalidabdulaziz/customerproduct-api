package com.customerproduct.customerproductapi.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionModel {

    private HttpStatus errorCode;
    private String userMessage;
    private String developerMessage;
}
