package org.fpf.governance.brain;

/**
 * Interface for the "Brain" or "Logic Engine" of the application.
 * Responsible for generating text responses from an AI model.
 */
public interface Brain {

    /**
     * Generates a text response for the given prompt.
     *
     * @param prompt The input text prompt.
     * @return The generated response text.
     * @throws RuntimeException if generation fails.
     */
    String generate(String prompt);
}
