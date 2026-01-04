package org.fpf.governance.checks;

import org.fpf.governance.vault.VaultProvider;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule201Assurance implements CheckRule {

    private static final Pattern TYPE_REGEX = Pattern.compile("^type:\\s*(\\w+)", Pattern.MULTILINE);

    // Checks for assurance block and its fields
    // assurance:
    // F: ...
    // G: ...
    // R_tuple: ...

    @Override
    public ValidationResult validate(String path, String content, VaultProvider vault) {
        Matcher typeMatch = TYPE_REGEX.matcher(content);
        if (!typeMatch.find()) {
            return new ValidationResult(true, List.of());
        }

        String type = typeMatch.group(1);
        if (!List.of("hypothesis", "decision").contains(type)) {
            return new ValidationResult(true, List.of());
        }

        if (!content.contains("assurance:")) {
            return new ValidationResult(false, List.of(
                    "RULE-201: Artifact of type '" + type + "' missing required 'assurance' block"));
        }

        // Simple string check for now. Ideally use a YAML parser.
        // We expect F:, G:, R_tuple: indented under assurance:
        // Or just somewhere in the file?
        // Assurance block is usually at the bottom.

        if (!Pattern.compile("F:\\s*").matcher(content).find() ||
                !Pattern.compile("G:\\s*").matcher(content).find() ||
                !Pattern.compile("R_tuple:\\s*").matcher(content).find()) {
            return new ValidationResult(false, List.of(
                    "RULE-201: Assurance block must contain 'F', 'G', and 'R_tuple' fields"));
        }

        return new ValidationResult(true, List.of());
    }
}
