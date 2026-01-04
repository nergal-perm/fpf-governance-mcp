# Design: Governance Rules & Vault Access

## Vault Access Strategy

To validate rules like `RULE-101` (Link references) and `RULE-202` (WIP Limits), the Server must perform "Cross-Artifact Validation". This requires read access to the FPF knowledge base.

### Options for Access

#### Option A: Local File System (Recommended)
The Server, being a local process (Native Image), can directly read the file system.
-   **Config**: Pass `--vault-path=/path/to/fpf` as a CLI argument or Env Var `FPF_VAULT_PATH`.
-   **Pros**: Simple, fast, no protocol overhead.
-   **Cons**: Requires the server to be running on the same machine (which it is).

#### Option B: MCP Resources Protocol
The Client (Host) exposes the Vault as MCP Resources, or the Server asks for `roots/list`.
-   **Pros**: More "Cloud/Remote" friendly.
-   **Cons**: Complexity. Requires Client to support specific capabilities. `check_file` needs synchronous random access to potentially thousands of files (for RULE-202 scanning). Network/Protocol overhead would be high.

### Data Access Layer
We will introduce a `VaultProvider` interface:
```java
interface VaultProvider {
    boolean exists(String relativePath);
    String readContent(String relativePath);
    List<Artifact> findArtifacts(ArtifactType type, String context); // For RULE-202
}
```
This isolates the validation logic from the IO mechanism.

## Rule Implementation Details

### RULE-002 (Lexical Firewall)
-   **Regex**: `^[A-Z]{3,4}-.*\.md$`
-   **Prefixes**: PROB, HYP, DEC, PROJ, EVID, CTX.
-   **Implementation**: Regex match on `Path.getFileName()`.

### The Chain of Reason (101-103)
-   **Parsing**: Need a robust Frontmatter parser (SnakeYAML is likely used or simple Regex).
-   **Link Resolution**: `parent_problem: "[[PROB-001]]"` or `parent_problem: "PROB-001"`. Need to handle Wiki-links format if used. Assuming standard paths or relative paths? FPF usually uses standard filenames.
-   **Logic**:
    1. Extract field value.
    2. Resolve to potential path (Search? or Direct Path?). If value is "PROB-001", we might need to find `20_Registry/Problems/PROB-001*.md`.
    3. `vault.exists(path)`.

### RULE-202 (Active Limit)
-   **Scope**: "Per Context". Context is usually defined by the directory (e.g., `50_Execution/Projects/ContextName/...`) or a tag?
-   **Definition**: "ONE PROJ can be status: in_progress per CTX".
-   **Algorithm**:
    1. Identify Context of current PROJ (e.g. Parent Folder).
    2. `vault.findFiles("50_Execution/Projects/" + context, type=PROJ)`.
    3. Count `status == in_progress`.
    4. If count > 1 (including current if being set to active) -> Error.

## Performance Considerations
-   **RULE-202** implies scanning. We should avoid scanning the entire disk on every check.
-   **Optimization**:
    -   Restrict scan to specific directories (`50_Execution`).
    -   Cache/Index if necessary? For now, OS file caching might be enough for a single user vault.
