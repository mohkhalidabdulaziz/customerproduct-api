package com.customerproduct.customerproductapi.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerServiceException extends RuntimeException {

    private String developerMessage;
    private String userMessage;
    private HttpStatus httpStatus;

}
