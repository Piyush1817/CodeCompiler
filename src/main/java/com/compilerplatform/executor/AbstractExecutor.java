package com.compilerplatform.executor;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.concurrent.TimeUnit;

public abstract class AbstractExecutor
        implements CodeExecutor {

    protected String readStream(
            InputStream stream
    ) throws Exception {

        return new String(
                stream.readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    protected void writeSourceCode(
            Path tempDir,
            String fileName,
            String code
    ) throws Exception {

        Path sourceFile =
                tempDir.resolve(
                        fileName
                );

        Files.writeString(
                sourceFile,
                code
        );
    }

    protected void sendInput(
            Process process,
            String input
    ) throws Exception {

        if (input != null
                && !input.isBlank()) {

            process.getOutputStream()
                    .write(
                            input.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            process.getOutputStream()
                    .flush();

            process.getOutputStream()
                    .close();
        }
    }

    protected void deleteDirectory(
            Path path
    ) {

        try {

            Files.walk(path)
                    .sorted(
                            (a, b) ->
                                    b.compareTo(a)
                    )
                    .forEach(file -> {

                        try {

                            Files.delete(file);

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    protected boolean waitForExecution(
            Process process
    ) throws Exception {

        return process.waitFor(
                5,
                TimeUnit.SECONDS
        );
    }
}