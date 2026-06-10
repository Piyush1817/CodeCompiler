package com.compilerplatform.executor;

import com.compilerplatform.dto.ExecuteResponse;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;


@Component
public class JavaExecutor extends AbstractExecutor {

    @Override
    public ExecuteResponse execute(String code, String input) {

        long startTime = System.currentTimeMillis(); //for total time
        Path tempDir = null;

        try {

            // Create temporary folder
            tempDir = Files.createTempDirectory("compiler-");

           //create Main.java and write code in Main.java
            writeSourceCode(
                    tempDir,
                    "Main.java",
                    code
            );

            // Compile
            Process compileProcess =
                    compileCode(tempDir);

            int compileExitCode =
                    compileProcess.waitFor();

            String compileErrors =
                    readStream(
                            compileProcess.getErrorStream()
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
                    System.currentTimeMillis(); //to calculate execution time
            // Execute
            Process runProcess =
                    executeCode(tempDir);

            // Send input
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
                        .error("Program execution exceeded 5 seconds.")
                        .executionTime(
                                System.currentTimeMillis()
                                        - executionStartTime
                        )
                        .build();
            }

            // Read normal output
            String output =
                    readStream(
                            runProcess.getInputStream()
                    );

            // Read runtime errors
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
                deleteDirectory(tempDir);
            }
        }
    }


    private Process compileCode(
            Path tempDir
    ) throws Exception {

        ProcessBuilder compileBuilder =
                new ProcessBuilder(
                        "javac",
                        "Main.java"
                );

        compileBuilder.directory(
                tempDir.toFile()
        );

        return compileBuilder.start();
    }

    private Process executeCode(
            Path tempDir
    ) throws Exception {

        ProcessBuilder runBuilder =
                new ProcessBuilder(
                        "java",
                        "Main"
                );

        runBuilder.directory(
                tempDir.toFile()
        );

        return runBuilder.start();
    }

    @Override
    public String getLanguage() {
        return "java";
    }
}