package org.fpf.governance.checks;

import org.fpf.governance.vault.VaultProvider;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Rule002Naming implements CheckRule {
    // Regex: Start with 3-4 uppercase letters, followed by a hyphen, ending in .md
    private static final Pattern NAMING_PATTERN = Pattern.compile("^(PROB|HYP|DEC|PROJ|EVID|CTX)-.*\\.md$");

    // Type regex to determine if we should check this file
    private static final Pattern TYPE_REGEX = Pattern.compile("^type:\\s*(\\w+)", Pattern.MULTILINE);

    @Override
    public ValidationResult validate(String pathStr, String content, VaultProvider vault) {
        // Only validate if it has a FPF type? Or validate all .md files in specific
        // folders?
        // The spec says: "Filenames MUST start with the correct semantic prefix."

        Matcher typeMatch = TYPE_REGEX.matcher(content);
        if (!typeMatch.find()) {
            return new ValidationResult(true, List.of()); // Not an FPF artifact
        }

        String type = typeMatch.group(1);
        // Only checking these types for now
        if (!List.of("dissatisfaction", "hypothesis", "decision", "project", "evidence", "context").contains(type)) {
            return new ValidationResult(true, List.of());
        }

        Path path = Paths.get(pathStr);
        String filename = path.getFileName().toString();

        if (!NAMING_PATTERN.matcher(filename).matches()) {
            return new ValidationResult(false, List.of(
                    "RULE-002: Naming violation. Artifact with type '" + type + "' must match pattern "
                            + NAMING_PATTERN.pattern() + ". Found: " + filename));
        }

        return new ValidationResult(true, List.of());
    }
}
