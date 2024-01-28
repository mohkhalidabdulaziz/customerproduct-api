package com.customerproduct.customerproductapi.exception;

import com.customerproduct.customerproductapi.utils.Constants;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.UnexpectedTypeException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        return new ResponseEntity<>(ExceptionModel.builder()
                .errorCode(HttpStatus.BAD_REQUEST)
                .userMessage("Error on field " + e.getBindingResult().getFieldError().getField())
                .developerMessage(Constants.ERROR_MESSAGE)
                .build(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionModel> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {

        return new ResponseEntity<>(ExceptionModel.builder()
                .errorCode(HttpStatus.BAD_REQUEST)
                .userMessage(String.format(Constants.INVALID_VALUE_ERROR_MESSAGE, e.getValue(), e.getName()))
                .developerMessage(Constants.ERROR_MESSAGE)
                .build(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = UnexpectedTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionModel> handleUnexpectedTypeException(UnexpectedTypeException e) {

        return new ResponseEntity<>(ExceptionModel.builder()
                .errorCode(HttpStatus.BAD_REQUEST)
                .userMessage(e.getLocalizedMessage())
                .developerMessage(Constants.ERROR_MESSAGE)
                .build(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionModel> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        String errorDetails = "Unacceptable JSON " + exception.getMessage();

        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException formatException = (InvalidFormatException) exception.getCause();
            errorDetails =
                    String.format(Constants.INVALID_VALUE_ERROR_MESSAGE, formatException.getValue(),
                            formatException.getPath().get(formatException.getPath().size() - 1).getFieldName());
            if (formatException.getTargetType() != null && formatException.getTargetType().isEnum()) {
                errorDetails = errorDetails.concat(String.format(" The value must be one of: %s.",
                        Arrays.toString(formatException.getTargetType().getEnumConstants())));
            }
        }

        return new ResponseEntity<>(ExceptionModel.builder()
                .errorCode(HttpStatus.BAD_REQUEST)
                .userMessage(errorDetails)
                .developerMessage(Constants.ERROR_MESSAGE)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionModel> handleSqlIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException e) {

        return new ResponseEntity<>(ExceptionModel.builder()
                .errorCode(HttpStatus.CONFLICT)
                .userMessage(e.getLocalizedMessage())
                .developerMessage(SQLIntegrityConstraintViolationException.class.getName())
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = CustomerServiceException.class)
    public ResponseEntity<ExceptionModel> handleCustomerServiceException(CustomerServiceException e) {
        return new ResponseEntity<>(ExceptionModel.builder()
                .errorCode(e.getHttpStatus())
                .userMessage(e.getUserMessage())
                .developerMessage(e.getDeveloperMessage())
                .build(), e.getHttpStatus());
    }


    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionModel> handleRuntimeException(
            RuntimeException e) {

        return new ResponseEntity<>(ExceptionModel.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .userMessage(e.getLocalizedMessage())
                .developerMessage(Constants.ERROR_MESSAGE)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
