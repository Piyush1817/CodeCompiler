package com.compilerplatform.service;

import com.compilerplatform.dto.ExecuteRequest;
import com.compilerplatform.dto.ExecuteResponse;

public interface ExecutionService {

    ExecuteResponse execute(ExecuteRequest request);
}