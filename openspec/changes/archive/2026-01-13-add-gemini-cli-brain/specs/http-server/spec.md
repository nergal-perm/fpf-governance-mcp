## ADDED Requirements

### Requirement: Manual Brain Test UI
The web interface SHALL provide a simple tool for the user to manually verify connectivity to the AI Brain.

#### Scenario: User submits a prompt
- **WHEN** the user enters text into the test prompt area and clicks "Send"
- **THEN** the system sends the prompt to the configured Brain
- **AND** displays the generated response text in the interface without a full page reload
