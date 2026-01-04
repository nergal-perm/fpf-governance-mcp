# Implement Governance Rules

## Change ID
`implement-governance-rules`

## Summary
Implement the remaining governance rules (Topology, Linkage, State/Integrity) in the `validate_governance_compliance` tool to enforce FPF methodology constraints. This requires granting the server access to the full FPF Knowledge Base (Vault) to validate relationships between artifacts (e.g., measuring link validity, checking active project limits).

## Problem
Currently, the MCP server only validates one rule (RULE-001 Location). It misses critical specific FPF rules:
1.  **Lexical Firewall (RULE-002)**: Strict naming conventions.
2.  **Chain of Reason (RULE-101..103)**: Logical lineage (Problem -> Hypothesis -> Decision -> Project).
3.  **Reality Check (RULE-201..203)**: Schema integrity, Work-in-Progress (WIP) limits, and staleness checks.

Without these, the "Governance" server does not actually govern the system's integrity, allowing "illegal" states (e.g., orphan Projects, invalid Frontmatter).

## Solution
1.  **Refactor Validator Engine**: Introduce a `VaultContext` or `GovernanceEngine` that can read/query the entire vault, not just the single file being checked.
2.  **Implement Rules**: Add checks for Regex naming, Link resolution (checking `parent_problem` exists), Schema validation (Assurance block), and Context-wide queries (counting active projects).
3.  **Vault Access**: Configure the server with read access to the FPF Vault (options detailed in `design.md`).

## Impact
-   **Security**: Minimal (Read-only access to notes).
-   **Performance**: RULE-202 (Active Limit) requires scanning the vault or maintaining an index. We must ensure this is fast (e.g., caching or optimized directory walking).
-   **Compat**: No breaking changes to the tool interface, but the *Server* configuration will change (requires Vault Path).
