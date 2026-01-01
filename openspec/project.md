# Project Context

## Purpose
The FPF Governance MCP Server provides tools to manage and govern a First Principles Framework (FPF) knowledge base. It exposes MCP resources and tools to interact with the FPF specification and user data.

## Tech Stack
- **Language**: Kotlin 2.0+
- **Build System**: Maven (Wrapper)
- **Runtime**: Java 21 LTS
- **Distribution**: GraalVM Native Image

## Project Conventions

### Code Style
- Follow official Kotlin coding conventions.
- Use `mvnw` for all build operations.
- **Build Command**: Always use `./mvnw -Pnative package` to generate the standalone executable.
- **Run Command**: Execute the binary directly: `./target/fpf-governance-mcp`.

### Architecture Patterns
- **MCP Server**: Implements the Model Context Protocol.
- **Hexagonal/Clean Architecture**: (Planned) Separate domain logic from adapters.

### Testing Strategy
- **Unit Tests**: JUnit 5 + Kotlin Test.
- **Integration Tests**: Verify MCP protocol compliance.

### Git Workflow
- Feature branches (e.g. `feature/xyz`).
- OpenSpec workflow for significant changes.

## Domain Context
The First Principles Framework (FPF) is a rigorous self-management methodology. The server acts as a bridge between the raw markdown files of the FPF and AI agents.

## Important Constraints
- **Performance**: Must start quickly and have low memory footprint (Native Image).
- **Correctness**: Must strictly adhere to the FPF specification.

## External Dependencies
- **GraalVM**: Required for native image builds.
