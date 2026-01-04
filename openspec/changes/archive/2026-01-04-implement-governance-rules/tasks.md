# Tasks

1.  [ ] **Design Vault Access**: Decide and implement the mechanism for the server to read the FPF Vault (CLI Argument vs Auto-discovery).
    *   *Validation*: Server starts and can list files in the vault.
2.  [ ] **Refactor CheckRule Interface**: Update `CheckRule` (or Registry) to accept `VaultContext` (allowing file lookup/reading).
    *   *Validation*: Unit test mocking the Context.
3.  [ ] **Implement RULE-002 (Thinking)**: Regex validation for filenames.
    *   *Validation*: Test with valid (`PROB-Desc.md`) and invalid (`Unstructured.md`) names.
4.  [ ] **Implement RULE-101 (Abductive Gate)**: Check `parent_problem` link existence.
    *   *Validation*: Test Hypo linking to non-existent Problem (Fail).
5.  [ ] **Implement RULE-102 (Decision Gate)**: Check `parent_hypothesis` link existence.
6.  [ ] **Implement RULE-103 (Execution Gate)**: Check `driven_by` link existence.
7.  [ ] **Implement RULE-201 (Assurance)**: Validate F, G, R fields in frontmatter.
8.  [ ] **Implement RULE-202 (Active Limit)**: Scan for `status: in_progress` Projects in the same Context.
    *   *Validation*: Create 2 active projects in same context, assert failure.
9.  [ ] **Implement RULE-203 (Staleness)**: Check MTime and EVID links.
10. [ ] **Verify E2E**: Run `validate_governance_compliance` on a complex real-world scenario.
