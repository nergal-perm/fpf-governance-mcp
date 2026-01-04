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

The project source code MUST be written in Kotlin (Version 2.0+).

#### Scenario: Source file compilation

  - **Given** a source file `src/main/kotlin/org/fpf/governance/Main.kt`
  - **When** the build runs
  - **Then** the Kotlin compiler compiles the code to bytecode compatible with Java 21.

### Requirement: Native Image Capability

The project MUST support building a standalone native executable using GraalVM.

#### Scenario: Build Native Image

  - **Given** GraalVM is available in the environment
  - **When** `./mvnw -Pnative package` is executed
  - **Then** a standalone binary is produced in the `target/` directory.
