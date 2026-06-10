package com.compilerplatform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice  //"Listen for exceptions thrown from any controller."
public class GlobalExceptionHandler {

    @ExceptionHandler(   //"Whenever this specific exception occurs, call this method."
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ValidationErrorResponse>
    handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ValidationErrorResponse response =
                ValidationErrorResponse
                        .builder()
                        .status(
                                "VALIDATION_ERROR"
                        )
                        .errors(
                                errors
                        )
                        .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }
}