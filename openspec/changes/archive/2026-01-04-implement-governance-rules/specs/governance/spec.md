## ADDED Requirements

### Requirement: Topology & Naming Rules (RULE-002)

The system MUST enforce lexical typing on filenames to ensure semantic clarity (Lexical Firewall).

#### Scenario: Valid Naming
- **Given** a content with `type: problem`
- **When** checking file path `20_Registry/Problems/PROB-MyIssue.md`
- **Then** the validation succeeds.

#### Scenario: Invalid Naming
- **Given** a content with `type: problem`
- **When** checking file path `20_Registry/Problems/JustSomeProblem.md`
- **Then** the validation fails with "RULE-002: Filename must start with PROB-".

### Requirement: Linkage & Causality Rules (RULE-101..103)

The system MUST enforce the "Chain of Reason" by requiring valid upstream links.

#### Scenario: RULE-101 (Hypothesis needs Problem)
- **Given** a Hypothesis artifacts defined in `content` with `parent_problem: "[[PROB-001]]"`
- **And** the file `20_Registry/Problems/PROB-001.md` exists in the vault
- **When** checking the Hypothesis
- **Then** the validation succeeds.

#### Scenario: RULE-101 Missing Link
- **Given** a Hypothesis with `parent_problem: "[[PROB-999]]"`
- **And** the file `PROB-999` does NOT exist
- **Then** the validation fails with "RULE-101: Upstream problem 'PROB-999' not found".

#### Scenario: RULE-102 (Decision needs Hypothesis)
- **Given** a Decision without `parent_hypothesis` field
- **Then** validation fails with "RULE-102: Missing parent_hypothesis".

### Requirement: State & Integrity Rules (RULE-201..203)

The system MUST enforce reality checks on state transitions and field integrity.

#### Scenario: RULE-201 (Assurance Check)
- **Given** a Hypothesis without an `assurance` block
- **Then** validation fails with "RULE-201: Missing assurance block".

#### Scenario: RULE-202 (Active Limit)
- **Given** the Context `ProjectAlpha` already has a Project in `status: in_progress`
- **When** attempting to set another Project in `ProjectAlpha` to `status: in_progress`
- **Then** validation fails with "RULE-202: Context active limit reached".

#### Scenario: RULE-203 (Staleness)
- **Given** a Project with `status: in_progress`
- **And** modification time is > 7 days ago
- **And** no linked Evidence < 7 days old
- **Then** validation fails with "RULE-203: Artifact is stale".
