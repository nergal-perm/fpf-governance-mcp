package org.fpf.governance.brain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Implementation of Brain that uses the local Gemini CLI.
 */
@Component
public class GeminiCliBrain implements Brain {

    private final String geminiPath;
    private final ProcessFactory processFactory;

    public interface ProcessFactory {
        Process start(String... command) throws IOException;
    }

    @Autowired
    public GeminiCliBrain(@Value("${fpf.brain.gemini.path:gemini}") String geminiPath) {
        this(geminiPath, command -> new ProcessBuilder(command).start());
    }

    GeminiCliBrain(String geminiPath, ProcessFactory processFactory) {
        this.geminiPath = geminiPath;
        this.processFactory = processFactory;
    }

    @Override
    public String generate(String prompt) {
        try {
            Process process = processFactory.start(geminiPath);

            // Write prompt to stdin
            try (OutputStream os = process.getOutputStream()) {
                os.write(prompt.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Read response from stdout
            String output;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                output = reader.lines().collect(Collectors.joining("\n"));
            }
            
            // Read stderr (for logging/debugging)
            String error;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                error = reader.lines().collect(Collectors.joining("\n"));
            }

            boolean finished = process.waitFor(60, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                throw new RuntimeException("Gemini CLI timed out");
            }

            if (process.exitValue() != 0) {
                 throw new RuntimeException("Gemini CLI failed with exit code " + process.exitValue() + ": " + error);
            }

            return output != null ? output.trim() : "";

        } catch (IOException e) {
            throw new RuntimeException("Failed to invoke Gemini CLI", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for Gemini CLI", e);
        }
    }
}
