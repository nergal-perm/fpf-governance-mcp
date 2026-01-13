package org.fpf.governance.brain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeminiCliBrainTest {

    @Mock
    private GeminiCliBrain.ProcessFactory processFactory;

    @Mock
    private Process process;

    @Test
    void generate_success() throws IOException, InterruptedException {
        // Setup
        String prompt = "Hello";
        String expectedResponse = "World";
        String geminiPath = "gemini";

        when(processFactory.start(geminiPath)).thenReturn(process);
        
        // Mock Stdin (we write to it)
        ByteArrayOutputStream processStdin = new ByteArrayOutputStream();
        when(process.getOutputStream()).thenReturn(processStdin);

        // Mock Stdout (we read from it)
        InputStream processStdout = new ByteArrayInputStream(expectedResponse.getBytes(StandardCharsets.UTF_8));
        when(process.getInputStream()).thenReturn(processStdout);

        // Mock Stderr
        InputStream processStderr = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
        when(process.getErrorStream()).thenReturn(processStderr);

        when(process.waitFor(60, TimeUnit.SECONDS)).thenReturn(true);
        when(process.exitValue()).thenReturn(0);

        GeminiCliBrain brain = new GeminiCliBrain(geminiPath, processFactory);
        String result = brain.generate(prompt);

        assertEquals(expectedResponse, result);
        assertEquals(prompt, processStdin.toString(StandardCharsets.UTF_8));
        verify(processFactory).start(geminiPath);
    }
}
