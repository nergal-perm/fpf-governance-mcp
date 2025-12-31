# Tasks

1. [ ] **Initialize Maven Project**
   - Generate standard directory layout (`src/main/kotlin`, `src/test/kotlin`).
   - Create root `pom.xml`.
   - Install Maven Wrapper (`mvnw`).

2. [ ] **Configure Kotlin**
   - Add `kotlin-maven-plugin` to `pom.xml`.
   - Set `kotlin.version` to 2.0.0+.
   - Configure dependencies (`kotlin-stdlib`).

3. [ ] **Configure GraalVM**
   - Add `native-maven-plugin` to `pom.xml`.
   - Configure a `native` profile for building the image to avoid overhead during standard dev cycles.

4. [ ] **Create Verification Entrypoint**
   - Create `src/main/kotlin/org/fpf/governance/Main.kt`.
   - Implement a simple "Hello FPF" to verify the toolchain.

5. [ ] **Update Project Documentation**
   - Update `openspec/project.md` to reflect the chosen Tech Stack.
