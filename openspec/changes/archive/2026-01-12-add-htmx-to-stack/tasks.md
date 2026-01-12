# HTMX Integration Tasks

- [x] **Update Project Vision**: Modify `openspec/project.md` to include HTMX in the tech stack.
    *   *Validation*: `openspec validate add-htmx-to-stack` passes.
- [x] **Update HTTP Server Spec**: Add requirements for HTML fragment serving and HTMX support to `openspec/specs/http-server/spec.md`.
    *   *Validation*: `openspec validate add-htmx-to-stack` passes.
- [x] **Add Dependencies**: Add `spring-boot-starter-thymeleaf` and HTMX (via WebJars or static file) to `pom.xml`.
    *   *Validation*: `mvn clean compile` passes.
- [x] **Cleanup Startup Logic**: Remove the `verifyNeo4jConnection` CommandLineRunner bean from `GovernanceApplication.java`.
    *   *Validation*: App starts without the specific "Neo4j Connection" log message.
- [x] **Implement Web Interface**: Create `src/main/resources/templates/index.html` with a basic layout and the "Check Status" button using `hx-get="/status/db"` and `hx-swap="outerHTML"`.
    *   *Validation*: Access `http://localhost:8080/` and see the button.
- [x] **Implement Status Controller**: Create a controller handling `GET /status/db` that attempts a Neo4j connection and returns an HTML fragment (e.g., `<span class="text-green-500">Connected</span>`).
    *   *Validation*: Clicking the button updates the UI with the status.
