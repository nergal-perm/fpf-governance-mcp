# http-server Specification

## ADDED Requirements

### Requirement: Frame & Deconstruct UI
The web interface SHALL provide a specialized view for the "Frame & Deconstruct" activity.

#### Scenario: Split Pane Layout
- **WHEN** the user accesses the "Frame" activity
- **THEN** the page displays two distinct columns: Input (1/3 width) and Response (2/3 width)

#### Scenario: Deconstruct Logic
- **WHEN** the user submits a situation description
- **THEN** the system invokes the Logic Engine with the `fpf:deconstruct` command
- **AND** displays the resulting markdown in the Response column
