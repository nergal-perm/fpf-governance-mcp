# Governance Specification

## Purpose
Defines the rules and mechanisms for governing the FPF knowledge base structure.

## ADDED Requirements

### Requirement: Pre-write Validation

The system MUST provide a mechanism to validate file content and location before persistence.

#### Scenario: Check Valid File Location
-   **Given** a file content with `type: hypothesis`
-   **When** `check_file` is called with path `30_Laboratory/Hypotheses/my-hypothesis.md`
-   **Then** the tool returns a valid result.

#### Scenario: Check Invalid File Location
-   **Given** a file content with `type: hypothesis`
-   **When** `check_file` is called with path `99_Trash/my-hypothesis.md`
-   **Then** the tool returns an invalid result with an error message referencing the violation.

### Requirement: Location Rules (RULE-001)

Artifacts MUST be placed in folders corresponding to their type:
-   `type: dissatisfaction` must be in `20_Registry/Problems`
-   `type: hypothesis` must be in `30_Laboratory/Hypotheses`
-   `type: decision` must be in `40_Governance/Decisions`
-   `type: project` must be in `50_Execution/Projects`

#### Scenario: Rule 001 Validation
-   **Given** a file content with `type: dissatisfaction`
-   **When** checking against `20_Registry/Problems/problem.md`
-   **Then** the location rule requires no errors.
