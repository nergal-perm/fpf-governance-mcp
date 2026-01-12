# foundation Specification

## Purpose
The Foundation specification defines the core technological stack, build systems, and runtime environments required for the FPF Governance MCP server. It ensures a consistent, high-performance, and maintainable codebase across all development environments, mandating Kotlin, Maven, and GraalVM Native Image support.

## Requirements

### Requirement: Build System

The project MUST be built using Apache Maven.

#### Scenario: Developer runs ./mvnw clean package

  - **Given** the repository includes `pom.xml` and `mvnw`
  - **When** the command is executed
  - **Then** the application builds successfully without requiring a system-level Maven installation.

### Requirement: Programming Language

The project source code MUST be written in Java (Version 21 LTS).

#### Scenario: Source file compilation

  - **Given** a source file `src/main/java/org/fpf/governance/GovernanceApplication.java`
  - **When** the build runs
  - **Then** the Java compiler compiles the code to bytecode compatible with Java 21.

### Requirement: Graph Database

The project MUST use Neo4j as the persistent storage engine.

#### Scenario: Database Connectivity

- **Given** a running Neo4j instance provided via Docker
- **When** the application starts
- **Then** it establishes a connection to the Neo4j database using the configured credentials.
