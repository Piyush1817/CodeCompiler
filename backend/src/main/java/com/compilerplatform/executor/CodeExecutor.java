package com.compilerplatform.executor;

import com.compilerplatform.dto.ExecuteResponse;

public interface CodeExecutor {

    ExecuteResponse execute(
            String code,
            String input
    );

    String getLanguage();
}