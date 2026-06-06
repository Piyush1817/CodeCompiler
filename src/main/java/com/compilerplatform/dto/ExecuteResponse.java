package com.compilerplatform.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecuteResponse {

    private String status;

    private String output;

    private String error;

    private long executionTime;
}