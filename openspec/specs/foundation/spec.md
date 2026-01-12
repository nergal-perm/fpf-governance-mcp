# Foundation Specification Delta

## MODIFIED Requirements

### Requirement: Programming Language

The project source code MUST be written in Java (Version 21 LTS).

#### Scenario: Source file compilation

  - **Given** a source file `src/main/java/org/fpf/governance/GovernanceApplication.java`
  - **When** the build runs
  - **Then** the Java compiler compiles the code to bytecode compatible with Java 21.

## ADDED Requirements

### Requirement: Graph Database

The project MUST use Neo4j as the persistent storage engine.

#### Scenario: Database Connectivity

  - **Given** a running Neo4j instance provided via Docker
  - **When** the application starts
  - **Then** it establishes a connection to the Neo4j database using the configured credentials.
