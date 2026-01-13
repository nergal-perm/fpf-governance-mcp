## Context
The application runs locally on a user's machine. The user is expected to have the `gemini` CLI installed and authenticated. We want to reuse this existing credential/session state instead of managing API keys within the Java app.

## Goals / Non-Goals
- **Goal:** simple, synchronous text generation via the CLI.
- **Non-Goal:** Streaming responses (for this MVP iteration, though the CLI supports it, we'll start with buffering).
- **Non-Goal:** Complex chat history management (the "Brain" just takes a prompt; context management is up to the caller).

## Decisions
- **Decision:** Use `ProcessBuilder` to invoke `gemini prompt <text>`.
    - **Rationale:** Simplest integration path. Avoids re-implementing auth/transport logic.
- **Decision:** Pass prompt as command line argument or stdin?
    - **Refinement:** Passing large prompts as arguments can hit shell limits. Stdin is safer.
    - **Selected Approach:** We will check if `gemini` supports reading from stdin. If not, we might need to use a temp file or careful argument escaping. *Self-correction: The `gemini` CLI usually accepts prompt as an argument. We will verify stdin support.*
- **Decision:** Use HTMX for the Manual Testing UI.
    - **Rationale:** Consistent with project tech stack. Allows testing the integration without full page reloads.

## Risks
- **Performance:** Spawning a JVM/Process for every request is slow.
    - **Mitigation:** Acceptable for MVP "Logic Audit" which is not high-frequency.
- **Environment:** CLI not found or not logged in.
    - **Mitigation:** Clear error messages.