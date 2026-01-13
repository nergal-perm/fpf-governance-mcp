## 1. Core Abstractions
- [x] 1.1 Define `Brain` interface (or `LogicEngine` interface) in `org.fpf.governance.brain`.
    - Method: `String generate(String prompt)`

## 2. Gemini CLI Implementation
- [x] 2.1 Create `GeminiCliBrain` class implementing `Brain`.
- [x] 2.2 Implement `ProcessBuilder` logic to call `gemini prompt "..."`.
    - Handle stdout (response).
    - Handle stderr (errors).
    - Handle timeout/exit codes.
- [x] 2.3 Add `gemini.cli.path` configuration property (default to "gemini").

## 3. Integration
- [x] 3.1 Register `GeminiCliBrain` as a Spring Bean (conditional on property?).
- [x] 3.2 Add Unit Tests for the adapter (mocking the process execution if possible, or separating the process logic).

## 4. UI Integration (Manual Test)
- [x] 4.1 Add `/test-brain` endpoint to `StatusController` (or new controller).
    - Accepts POST with prompt.
    - Calls `Brain.generate(prompt)`.
    - Returns HTML fragment with response.
- [x] 4.2 Update `index.html` to include the test form.
    - Textarea for prompt.
    - Button "Ask Brain".
    - `hx-post="/test-brain"`.
    - `hx-target="#brain-response"`.