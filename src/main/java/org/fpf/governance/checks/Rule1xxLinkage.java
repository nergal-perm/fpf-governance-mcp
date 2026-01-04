package org.fpf.governance.checks;

import org.fpf.governance.vault.VaultProvider;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule1xxLinkage implements CheckRule {

    private static final Map<String, LinkRequirement> REQUIREMENTS = Map.of(
            "hypothesis", new LinkRequirement("parent_problem", "PROB-", "20_Registry/Problems"),
            "decision", new LinkRequirement("parent_hypothesis", "HYP-", "30_Laboratory/Hypotheses"),
            "project", new LinkRequirement("driven_by", "DEC-", "40_Governance/Decisions"));

    // Regex to extract type
    private static final Pattern TYPE_REGEX = Pattern.compile("^type:\\s*(\\w+)", Pattern.MULTILINE);

    @Override
    public ValidationResult validate(String path, String content, VaultProvider vault) {
        Matcher typeMatch = TYPE_REGEX.matcher(content);
        if (!typeMatch.find()) {
            return new ValidationResult(true, List.of());
        }

        String type = typeMatch.group(1);
        LinkRequirement req = REQUIREMENTS.get(type);

        if (req == null) {
            return new ValidationResult(true, List.of());
        }

        // Find field
        Pattern fieldPattern = Pattern.compile("^" + req.field + ":\\s*\"?(?:\\[\\[)?([A-Z]+-[^\\]\"]+)(?:\\]\\])?\"?",
                Pattern.MULTILINE);
        Matcher fieldMatch = fieldPattern.matcher(content);

        if (!fieldMatch.find()) {
            return new ValidationResult(false, List.of(
                    "RULE-10x: Artifact of type '" + type + "' missing required field '" + req.field + "'"));
        }

        String linkedId = fieldMatch.group(1);

        // Link must start with expected prefix
        if (!linkedId.startsWith(req.expectedPrefix)) {
            return new ValidationResult(false, List.of(
                    "RULE-10x: Field '" + req.field + "' must link to an artifact starting with '" + req.expectedPrefix
                            + "'. Found: '" + linkedId + "'"));
        }

        // Check existence in vault
        // We search for file starting with the ID (assuming ID is the filename prefix
        // or full filename)
        // If linkedId is "PROB-001", we look for "PROB-001*.md" in targetDir.

        // Escape regex special chars in linkedId just in case, though they are usually
        // safe A-Z0-9-
        String searchPattern = "^" + Pattern.quote(linkedId) + ".*\\.md$";

        List<String> found = vault.findFiles(req.targetDir, searchPattern);

        if (found.isEmpty()) {
            return new ValidationResult(false, List.of(
                    "RULE-10x: Upstream artifact '" + linkedId + "' not found in '" + req.targetDir + "'"));
        }

        return new ValidationResult(true, List.of());
    }

    private static class LinkRequirement {
        final String field;
        final String expectedPrefix;
        final String targetDir;

        LinkRequirement(String field, String expectedPrefix, String targetDir) {
            this.field = field;
            this.expectedPrefix = expectedPrefix;
            this.targetDir = targetDir;
        }
    }
}
