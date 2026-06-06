package com.compilerplatform.service.impl;

import com.compilerplatform.dto.ExecuteRequest;
import com.compilerplatform.dto.ExecuteResponse;
import com.compilerplatform.executor.CodeExecutor;
import com.compilerplatform.service.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExecutionServiceImpl
        implements ExecutionService {

    private final List<CodeExecutor> executors;

    @Override
    public ExecuteResponse execute(
            ExecuteRequest request
    ) {

        Map<String, CodeExecutor> executorMap =
                new HashMap<>();

        for (CodeExecutor executor : executors) {

            executorMap.put(
                    executor.getLanguage(),
                    executor
            );
        }

        CodeExecutor selectedExecutor =
                executorMap.get(
                        request.getLanguage()
                                .toLowerCase()
                );

        if (selectedExecutor == null) {

            return ExecuteResponse.builder()
                    .status("ERROR")
                    .output("")
                    .error("Unsupported language.")
                    .executionTime(0)
                    .build();
        }

        return selectedExecutor.execute(
                request.getCode(),
                request.getInput()
        );
    }
}