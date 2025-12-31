# Design: Foundation Stack

## Architectural Decisions

### 1. Kotlin
**Why**: Kotlin provides a modern, concise syntax with null-safety features that reduce runtime errors. It interoperates fully with the Java ecosystem, allowing access to extensive libraries while offering a better developer experience.
**Version**: 2.0.0 or later (to leverage the K2 compiler).

### 2. Maven
**Why**: Maven is a widely supported, declarative build tool. It offers stability and standard directory layouts which reduces configuration overhead compared to Gradle for standard backend services.
**Configuration**: Standard `pom.xml` with the Maven Wrapper (`mvnw`) to ensure consistent build environments.

### 3. GraalVM Native Image
**Why**: As an MCP (Model Context Protocol) server, the application may be run locally by users or agents. Fast startup time and low memory footprint are critical for this use case. Native Image compiles the application ahead-of-time (AOT) to a standalone executable.
**Strategy**: Use the `native-maven-plugin` from GraalVM.

### 4. Java Version
**Why**: Java 21 is the current LTS. It provides Virtual Threads (Project Loom), which might be beneficial for high-throughput I/O if needed later.

## Constraints
- Reflection usage must be configured for Native Image compatibility (reachability metadata).
- Dependencies should be chosen for GraalVM compatibility where possible.
