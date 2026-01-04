package org.fpf.governance.checks;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule001Location implements CheckRule {
    @Override
    public ValidationResult validate(String path, String content) {
        Pattern typeRegex = Pattern.compile("^type:\\s*(\\w+)", Pattern.MULTILINE);
        Matcher match = typeRegex.matcher(content);

        String type = null;
        if (match.find()) {
            type = match.group(1);
        }

        if (type == null) {
            return new ValidationResult(true, List.of());
        }

        String expectedDir = switch (type) {
            case "dissatisfaction" -> "20_Registry/Problems";
            case "hypothesis" -> "30_Laboratory/Hypotheses";
            case "decision" -> "40_Governance/Decisions";
            case "project" -> "50_Execution/Projects";
            default -> null;
        };

        if (expectedDir != null) {
            if (!path.contains(expectedDir)) {
                return new ValidationResult(
                        false,
                        List.of("RULE-001: Artifact of type '" + type + "' must be located in '" + expectedDir + "'. Current path: '" + path + "'")
                );
            }
        }
        return new ValidationResult(true, List.of());
    }
}
