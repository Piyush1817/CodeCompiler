package com.compilerplatform.exception;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ValidationErrorResponse {

    private String status;

    private Map<String, String> errors;    //like language is required so code is required
}