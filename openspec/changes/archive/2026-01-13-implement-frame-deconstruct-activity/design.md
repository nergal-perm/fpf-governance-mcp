# Design: Frame & Deconstruct Activity

## Architecture

### Frontend (HTMX + Thymeleaf)
-   **Layout**: A 2-column CSS Grid or Flexbox layout.
    -   `#input-pane` (1/3): Contains a `<form>` with a `<textarea>`. Submits via `hx-post="/activity/frame/submit"`.
    -   `#response-pane` (2/3): A `<div>` that receives the HTML response.
-   **Markdown Rendering**: The response from the server will be raw text/markdown. A client-side library like `marked.js` will be used to render it.

### Backend (Java/Spring)

1.  **Prompt Template**:
    -   A new file will be created at `src/main/resources/prompts/deconstruct.txt`.
    -   This file will contain the system prompt for the deconstruction task, with a placeholder for the user's input (e.g., `{{USER_INPUT}}`).

2.  **FrameActivityController**:
    -   Endpoint: `POST /activity/frame/submit`
    -   Body: `description` (User input)
    -   Logic:
        1.  Load the `prompts/deconstruct.txt` resource as a string.
        2.  Replace the `{{USER_INPUT}}` placeholder with the user's `description`.
        3.  Call `brain.generate(enhancedPrompt)`.
        4.  Return a partial HTML fragment containing the raw markdown response from the brain.

3.  **No `GeminiCliBrain` Changes**:
    -   The existing `GeminiCliBrain` will be used as-is. It will receive the fully-formed prompt and pass it to the `gemini` CLI.