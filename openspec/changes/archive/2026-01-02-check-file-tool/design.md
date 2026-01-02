# Design: Check File Tool

## Problem
We need to enforce governance rules on files that may not exist on disk yet. The validation logic must be decoupled from the file system.

## Solution
Introduce a rule-based checking engine that the `check_file` tool invokes.

### Components
1.  **CheckRule Interface**: `interface CheckRule { fun validate(path: String, content: String): ValidationResult }`
    -   `ValidationResult`: `data class(val valid: Boolean, val errors: List<String>)`
2.  **CheckRegistry**: A singleton (similar to `ToolRegistry`) that holds a list of `CheckRule`s.
3.  **CheckFileTool**: An `McpTool` named `check_file`.
    -   Schema: `path` (string), `content` (string).
    -   Logic: Iterates through `CheckRegistry`, aggregating errors.
4.  **Rule001 (Location)**: Parses the YAML frontmatter (regex-based for now) to find `type` and asserts the `path` contains the correct directory segment.

### Rule 001 Logic
-   Extract `type: <value>` from frontmatter.
-   Map:
    -   `dissatisfaction` -> `20_Registry/Problems`
    -   `hypothesis` -> `30_Laboratory/Hypotheses`
    -   `decision` -> `40_Governance/Decisions`
    -   `project` -> `50_Execution/Projects`
-   Check if `path` contains the required substring.

### Trade-offs
-   **Regex Frontmatter Parsing**: We are still avoiding a full YAML parser dep. Regex will be sufficient for standard frontmatter `type: value` lines.
