package com.compilerplatform.executor;

import com.compilerplatform.dto.ExecuteResponse;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.charset.StandardCharsets;


@Component
public class JavaExecutor implements CodeExecutor {

    @Override
    public ExecuteResponse execute(String code, String input) {

        try {

            // Create temporary folder
            Path tempDir = Files.createTempDirectory("compiler-");

            // Create Main.java path
            Path javaFile = tempDir.resolve("Main.java");

            // Write user code into Main.java
            Files.writeString(javaFile, code);

            ProcessBuilder compileBuilder =
                    new ProcessBuilder(
                            "javac",
                            "Main.java"
                    );

            compileBuilder.directory(tempDir.toFile());

            Process compileProcess = compileBuilder.start(); //start compiling

            int compileExitCode = compileProcess.waitFor(); // wait for to complete compilation

            String compileErrors =
                    new String(
                            compileProcess
                                    .getErrorStream()
                                    .readAllBytes(),
                            StandardCharsets.UTF_8
                    );

            if (compileExitCode != 0) {

                return ExecuteResponse.builder()
                        .status("COMPILATION_ERROR")
                        .output("")
                        .error(compileErrors)
                        .executionTime(0)
                        .build();
            }

            ProcessBuilder runBuilder =
                    new ProcessBuilder(
                            "java",
                            "Main"
                    );
            runBuilder.directory(tempDir.toFile());
            Process runProcess =runBuilder.start();  //start execution

            if (input != null && !input.isBlank()) {

                runProcess.getOutputStream()
                        .write(input.getBytes(StandardCharsets.UTF_8));

                runProcess.getOutputStream()
                        .flush();

                runProcess.getOutputStream()
                        .close();
            }

            runProcess.waitFor(); //wait for execution to finish

            String output =
                    new String(
                            runProcess
                                    .getInputStream()
                                    .readAllBytes(),
                            StandardCharsets.UTF_8
                    );


            return ExecuteResponse.builder()
                    .status("SUCCESS")
                    .output(output)
                    .error("")
                    .executionTime(0)
                    .build();

        } catch (Exception e) {

            return ExecuteResponse.builder()
                    .status("ERROR")
                    .output("")
                    .error(e.getMessage())
                    .executionTime(0)
                    .build();
        }
    }
}