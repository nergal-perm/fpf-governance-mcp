## ADDED Requirements

### Requirement: LLM Text Generation
The system SHALL provide a mechanism to submit a text prompt to an LLM and receive a text response.

#### Scenario: Successful generation via Gemini CLI
- **WHEN** the system requests text generation with the prompt "Hello"
- **AND** the Gemini CLI is configured and available
- **THEN** the system returns a non-empty string response from the model

#### Scenario: CLI Unavailable
- **WHEN** the system requests text generation
- **AND** the Gemini CLI executable is missing or fails
- **THEN** the system throws a descriptive error indicating the infrastructure failure
