package com.compilerplatform.executor;

import com.compilerplatform.dto.ExecuteResponse;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Component
public class CppExecutor extends AbstractExecutor {

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
                    "main.cpp",
                    code
            );

            Process compileProcess =
                    compileCode(
                            tempDir
                    );

            int compileExitCode =
                    compileProcess.waitFor();


            String compileErrors =
                    readStream(
                            compileProcess
                                    .getErrorStream()
                    );

            if (compileExitCode != 0) {

                return ExecuteResponse.builder()
                        .status("COMPILATION_ERROR")
                        .output("")
                        .error(compileErrors)
                        .executionTime(
                                System.currentTimeMillis()
                                        - startTime
                        )
                        .build();
            }

            long executionStartTime =
                    System.currentTimeMillis();

            Process runProcess =
                    executeCode(
                            tempDir
                    );

            sendInput(
                    runProcess,
                    input
            );

            boolean finished =
                    runProcess.waitFor(
                            5,
                            TimeUnit.SECONDS
                    );

            if (!finished) {

                runProcess.destroyForcibly();

                return ExecuteResponse.builder()
                        .status("TIMEOUT")
                        .output("")
                        .error("Program execution exceeded 5 seconds.")
                        .executionTime(
                                System.currentTimeMillis()
                                        - executionStartTime
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
                                        - executionStartTime
                        )
                        .build();
            }

            return ExecuteResponse.builder()
                    .status("SUCCESS")
                    .output(output)
                    .error("")
                    .executionTime(
                            System.currentTimeMillis()
                                    - executionStartTime
                    )
                    .build();

        } catch (Exception e) {

            return ExecuteResponse.builder()
                    .status("ERROR")
                    .output("")
                    .error(e.getMessage())
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

    private Process compileCode(
            Path tempDir
    ) throws Exception {

        ProcessBuilder builder =
                new ProcessBuilder(
                        "g++",
                        "main.cpp",
                        "-o",
                        "main.exe"
                );

        builder.directory(
                tempDir.toFile()
        );

        return builder.start();
    }

    private Process executeCode(
            Path tempDir
    ) throws Exception {

        ProcessBuilder builder =
                new ProcessBuilder(
                        tempDir
                                .resolve("main.exe")
                                .toString()
                );

        return builder.start();
    }

    @Override
    public String getLanguage() {
        return "cpp";
    }
}