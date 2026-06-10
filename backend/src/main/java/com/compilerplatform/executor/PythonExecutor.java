package com.compilerplatform.executor;

import com.compilerplatform.dto.ExecuteResponse;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class PythonExecutor
        extends AbstractExecutor {

    @Override
    public ExecuteResponse execute(
            String code,
            String input
    ) {

        long startTime =
                System.currentTimeMillis();

        Path tempDir = null;

        try {

            tempDir =
                    Files.createTempDirectory(
                            "compiler-"
                    );

            writeSourceCode(
                    tempDir,
                    "main.py",
                    code
            );

            Process runProcess =
                    executeCode(
                            tempDir
                    );

            sendInput(
                    runProcess,
                    input
            );

            boolean finished =
                    waitForExecution(
                            runProcess
                    );

            if (!finished) {

                runProcess.destroyForcibly();

                return ExecuteResponse.builder()
                        .status("TIMEOUT")
                        .output("")
                        .error(
                                "Program execution exceeded 5 seconds."
                        )
                        .executionTime(
                                System.currentTimeMillis()
                                        - startTime
                        )
                        .build();
            }

            String output =
                    readStream(
                            runProcess.getInputStream()
                    );

            String runtimeErrors =
                    readStream(
                            runProcess.getErrorStream()
                    );

            if (!runtimeErrors.isBlank()) {

                return ExecuteResponse.builder()
                        .status("RUNTIME_ERROR")
                        .output("")
                        .error(runtimeErrors)
                        .executionTime(
                                System.currentTimeMillis()
                                        - startTime
                        )
                        .build();
            }

            return ExecuteResponse.builder()
                    .status("SUCCESS")
                    .output(output)
                    .error("")
                    .executionTime(
                            System.currentTimeMillis()
                                    - startTime
                    )
                    .build();

        } catch (Exception e) {

            return ExecuteResponse.builder()
                    .status("ERROR")
                    .output("")
                    .error(
                            e.getMessage()
                    )
                    .executionTime(
                            System.currentTimeMillis()
                                    - startTime
                    )
                    .build();

        } finally {

            if (tempDir != null) {
                deleteDirectory(
                        tempDir
                );
            }
        }
    }
    private Process executeCode(
            Path tempDir
    ) throws Exception {

        ProcessBuilder runBuilder =
                new ProcessBuilder(
                        "python",
                        "main.py"
                );

        runBuilder.directory(
                tempDir.toFile()
        );

        return runBuilder.start();
    }

    @Override
    public String getLanguage() {
        return "python";
    }
}