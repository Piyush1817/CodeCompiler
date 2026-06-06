package com.compilerplatform.controller;

import com.compilerplatform.dto.ExecuteRequest;
import com.compilerplatform.dto.ExecuteResponse;
import com.compilerplatform.service.ExecutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;

    @PostMapping("/execute")
    public ResponseEntity<ExecuteResponse> execute(
            @Valid @RequestBody ExecuteRequest request
    ) {

        ExecuteResponse response =
                executionService.execute(request);

        return ResponseEntity.ok(response);
    }
}