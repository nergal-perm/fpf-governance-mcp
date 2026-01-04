package org.fpf.governance.checks;

import org.fpf.governance.vault.VaultProvider;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Rule202ActiveLimit implements CheckRule {

    private static final Pattern TYPE_REGEX = Pattern.compile("^type:\\s*(\\w+)", Pattern.MULTILINE);
    private static final Pattern STATUS_REGEX = Pattern.compile("^status:\\s*(\\w+)", Pattern.MULTILINE);

    @Override
    public ValidationResult validate(String pathStr, String content, VaultProvider vault) {
        Matcher typeMatch = TYPE_REGEX.matcher(content);
        if (!typeMatch.find()) {
            return new ValidationResult(true, List.of());
        }

        String type = typeMatch.group(1);
        if (!"project".equals(type)) {
            return new ValidationResult(true, List.of());
        }

        Matcher statusMatch = STATUS_REGEX.matcher(content);
        if (!statusMatch.find()) {
            // Missing status might be a schema violation but handled elsewhere?
            // Or assume not active.
            return new ValidationResult(true, List.of());
        }

        String status = statusMatch.group(1);
        if (!"in_progress".equals(status)) {
            return new ValidationResult(true, List.of());
        }

        // It is an active project. Check if others exist in the same context.
        Path path = Paths.get(pathStr);
        String parentDir = path.getParent() != null ? path.getParent().toString() : "";

        // Scan for other projects in this dir
        // We look for any file ending in .md
        List<String> files = vault.findFiles(parentDir, ".*\\.md$");

        // We start with 1 because the current artifact (content) is explicitly
        // 'in_progress' (checked above)
        int activeCount = 1;
        for (String file : files) {
            // Skip the file we are currently checking ("self")
            // We rely on the content passed in, not what's on disk.
            if (pathStr.endsWith(file)) {
                continue;
            }

            String fileContent = vault.readContent(parentDir.isEmpty() ? file : parentDir + "/" + file);
            if (isProjectInProgress(fileContent)) {
                activeCount++;
            }
        }

        if (activeCount > 1) {
            return new ValidationResult(false, List.of(
                    "RULE-202: Active limit reached. Context '" + parentDir + "' already has an active project."));
        }

        return new ValidationResult(true, List.of());
    }

    private boolean isProjectInProgress(String content) {
        Matcher t = TYPE_REGEX.matcher(content);
        if (t.find() && "project".equals(t.group(1))) {
            Matcher s = STATUS_REGEX.matcher(content);
            return s.find() && "in_progress".equals(s.group(1));
        }
        return false;
    }
}
