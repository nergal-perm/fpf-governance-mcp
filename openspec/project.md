# Project Vision: The AI Governance Agent

## 1. Context & Core Philosophy

- **The Problem:** The "Execution Gap." High-level knowledge workers ("Stuck Visionaries") suffer from endless ideation but lack the external pressure to execute personal projects. They drift into "fake work" (researching, planning) instead of tangible output.
- **The Solution:** An AI-powered **Governance Agent**. It acts not as a servant, but as a **Partner and Auditor**. It uses formal logic to deconstruct vague ideas into rigorous plans and enforces execution through a session-based feedback loop. The solution is based on the FPF methodology.
The First Principles Framework (FPF) is a rigorous self-management and thinking methodology. The server acts as a bridge between the raw markdown files of the FPF and AI agents.
- **The Persona:** **"The Socratic Mentor."** The AI is curious, questioning, and guiding. It does not blindly accept user input; it challenges logical fallacies and drift.

## 2. System Architecture & Constraints

- **The Brain (Logic Engine):** A custom AI framework (already existing) that applies formal logic/rigor to natural language inputs. It detects category errors, logical gaps, and validates hypotheses.
- **The Interface (Hybrid):**
    - **Web App:** The "Board Room." Used for high-level planning, logic deconstruction, and viewing the "Truth" (Knowledge Base).
    - **The FPF Governance MCP Server**: an HTTP-based server that provides tools to manage and govern a First Principles Framework (FPF) knowledge base. It exposes MCP resources and tools via an SSE (Server-Sent Events) transport to interact with the FPF specification and user data.
- **The Single Source of Truth:** A **Read-Only Knowledge Base** (Text-based Dashboard for MVP). The user _cannot_ directly edit the plan or status. They must engage in a dialogue with the Agent to change the state of the system.

### Tech Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.2+
- **Build System**: Maven (Wrapper)
- **Runtime**: Java 21 LTS
- **Distribution**: JAR / Container

### Code Style
- Follow official Java/Spring coding conventions.
- Use `mvnw` for all build operations.
- **Build Command**: ` ./mvnw clean package`
- **Run Command**: `java -jar target/fpf-governance-mcp-0.1.0-SNAPSHOT.jar`

### Architecture Patterns
- **MCP Server**: Implements the Model Context Protocol over HTTP/SSE.
- **Hexagonal/Clean Architecture**: (Planned) Separate domain logic from adapters.

### Testing Strategy
- **Unit Tests**: JUnit 5.
- **Integration Tests**: Verify MCP protocol compliance.

### Git Workflow
- Feature branches (e.g. `feature/xyz`).
- OpenSpec workflow for significant changes.

## 3. User Story Map: Release 1 (MVP)

### Activity 1: Frame & Deconstruct (Web App)

_Goal: Move from vague dream to rigorous problem statement._

- **User Task:** **Brain Dump.** User enters a vague idea/problem into the Web Chat.
- **System Task:** **Logic Audit.** Agent parses input using the Formal Logic Framework to identify fallacies, vague terms, or logic gaps.
- **System Task:** **Deconstruction Dialogue.** Agent engages the user Socratically to refine the input (e.g., "You stated X, but X implies Y. Is Y true?").
- **Outcome:** **Formal Statement Consensus.** The Agent saves a locked, rigorous "Problem Statement" to the Knowledge Base.

### Activity 2: Plan & Commit (Web App)

_Goal: Define the Minimum Viable Solution based on logic._

- **System Task:** **Hypothesis Abduction.** Agent generates potential solution hypotheses based on the Problem Statement.
- **User Task:** **Selection.** User selects the strongest hypothesis to pursue.
- **User & System:** **MVP Definition.** Collaborative chat to define the "Smallest Scope" required to validate the hypothesis.
- **System Task:** **Task Generation.** Agent breaks the scope into a linear, prioritized list of tasks.
- **User Task:** **Commitment.** User explicitly "Commits" to the plan. This **locks** the node in the Knowledge Base.

### Activity 3: The Session Loop (Telegram Bot)

_Goal: Execute work in short, governed bursts._

- **User Task:** **Intention Ping.** User messages Bot: "Starting work."
- **System Task:** **Why-Inquiry.** Bot asks: "What are you doing, and which Task does this support?"
- **System Task:** **Timer Set.** Bot initiates a focus block (e.g., 45/60 mins).
- **User Task:** **Work Block.** User performs real-world actions (offline).
- **User Task:** **Result Log.** Timer fires. User replies with a text summary of exactly what was produced.

### Activity 4: Govern & Audit (Web App + Bot Logic)

_Goal: Verify progress and detect drift._

- **System Task:** **Consistency Check.** Agent compares the **Result Log** against the **Intention** and the **Project Plan**.
- **System Task:** **Socratic Review.**
    - _If Valid:_ Agent updates progress.
    - _If Drift Detected:_ Agent challenges the user (e.g., "You researched fonts, but the plan was to write code. How does this validate our hypothesis?").
- **User Task:** **Dashboard Review.** User views the Web Dashboard to see their "Current Truth" (Plan Status, Hypotheses, Evidence) in a read-only text format.

## 4. Technical Guidelines for the Coding Agent

### The "Read-Only" Rule

- The database entities for `Problem`, `Hypothesis`, `Plan`, and `Task` are **immutable** via direct user CRUD operations.
- State changes (e.g., changing a Task from "Todo" to "Done", or deleting a Hypothesis) can **only** be triggered by the Agent function after a successful validation dialogue.
    

### The "Socratic" Prompting Strategy

- When generating responses, the Agent must never be passive.
- If the user provides a lazy answer, the Agent must probe deeper.
- _Tone:_ Professional, logical, firm, but supportive. Think: A kind but strict philosophy professor.
    

### Future Scope (Ignored for MVP)

- Do not build: Visual node-graph visualizations (D3/Canvas). Use text lists/tables for now.
- Do not build: Direct integration with GitHub/Google Docs. Rely on user text logs (Honor System).
- Do not build: Multi-project support. Focus on single-threaded project governance.
