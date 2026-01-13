# FPF Governance MCP Server

## Project Overview

**Name:** `fpf-governance-mcp`
**Purpose:** An AI Governance Agent based on the First Principles Framework (FPF). It acts as a "Socratic Mentor" to help "Stuck Visionaries" execute projects by enforcing a session-based feedback loop (Intention -> Work -> Result -> Audit).
**Core Concept:** A "Read-Only Knowledge Base" where users cannot directly edit plans or status. All state changes occur through dialogue with the Agent, which acts as an auditor.

## Architecture & Tech Stack

*   **Language:** Java 21
*   **Framework:** Spring Boot 3.2+
*   **Protocol:** Model Context Protocol (MCP) over HTTP/SSE
*   **Database:** Neo4j Community Edition (Graph DB) & AWS DynamoDB
*   **Frontend:** HTMX + Thymeleaf (Server-Side Rendering)
*   **Build System:** Maven
*   **Containerization:** Docker

## Build & Run

**Prerequisites:** Java 21, Maven.

*   **Build:**
    ```bash
    ./mvnw clean package
    ```

*   **Run Locally:**
    ```bash
    java -jar target/fpf-governance-mcp-0.1.0-SNAPSHOT.jar
    ```

*   **Docker:**
    ```bash
    docker-compose up --build
    ```

## Development Conventions

### OpenSpec Workflow

This project uses **OpenSpec** for specification-driven development. AI agents must follow this strictly.

**1. Creating Changes (Proposal Stage)**
*   **Trigger:** New features, breaking changes, architecture shifts. (Skip for bug fixes/typos).
*   **Action:**
    1.  Choose a unique `change-id` (kebab-case, verb-led, e.g., `add-auth-layer`).
    2.  Scaffold directory: `openspec/changes/<change-id>/`.
    3.  Create `proposal.md`: Explain "Why", "What Changes", and "Impact".
    4.  Create `tasks.md`: Implementation checklist. Each task should be formatted as a Markdown task, i.e. `- [ ] **Write down a task**. Each task should be formatted properly`
    5.  Create `design.md`: (Optional) Only for complex architecture/decisions.
    6.  Create Spec Deltas (`specs/<capability>/spec.md`): Use `## ADDED`, `## MODIFIED`, `## REMOVED Requirements`. **Every requirement must have a `#### Scenario:`**.
    7.  **Validate:** `openspec validate <change-id> --strict`.

**2. Implementing Changes**
*   **Wait for Approval:** Do not start coding until the proposal is approved.
*   **Execute:** Follow `tasks.md` sequentially.
*   **Update:** Mark tasks as `[x]` in `tasks.md` as you complete them.

**3. Archiving Changes**
*   **Trigger:** Feature deployed and verified.
*   **Action:**
    ```bash
    openspec archive <change-id> --yes
    ```
    (This moves the change to `openspec/changes/archive/` and applies the deltas to the main specs).

### Coding Standards
*   Follow official Java/Spring Boot conventions.
*   Use `mvnw` for all build operations.
*   **Testing:** JUnit 5.
*   **"Read-Only" Rule:** Database entities are immutable via direct user CRUD. State changes only via Agent validation.
*   **"Socratic" Persona:** The Agent must be professional, logical, and firm.

## Key Directories
*   `openspec/`: Specifications and change proposals.
*   `src/main/java/org/fpf/governance/`: Main application code.
*   `src/main/resources/templates/`: Thymeleaf templates.
