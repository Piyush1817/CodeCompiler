package com.compilerplatform.service.impl;

import com.compilerplatform.dto.ExecuteRequest;
import com.compilerplatform.dto.ExecuteResponse;
import com.compilerplatform.executor.JavaExecutor;
import com.compilerplatform.service.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutionServiceImpl implements ExecutionService {

    private final JavaExecutor javaExecutor;

    @Override
    public ExecuteResponse execute(ExecuteRequest request) {

        return javaExecutor.execute(
                request.getCode(),
                request.getInput()
        );
    }
}