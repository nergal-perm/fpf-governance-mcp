# Change: Add Gemini CLI Brain

## Why
The project vision requires a "Logic Engine" to perform "Logic Audits" and engage in "Socratic Dialogue". Currently, the system has no connection to an LLM. We need to bridge the Java application with an AI model.

## What Changes
- Create a `LogicEngine` service interface for text generation.
- Implement a `GeminiCliBrain` adapter that invokes the `gemini` CLI tool as a subprocess.
- Add configuration to enable/configure the brain provider.
- Add a "Manual Testing UI" to the main web page (prompt text area + submit button) to verify LLM connectivity.

## Impact
- **New Capability:** `logic-engine`
- **Runtime Requirement:** The `gemini` CLI tool must be installed and authenticated on the host system.
