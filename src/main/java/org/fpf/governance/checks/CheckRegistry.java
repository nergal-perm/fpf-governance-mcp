package org.fpf.governance.checks;

import java.util.ArrayList;
import java.util.List;

public class CheckRegistry {
    private static final List<CheckRule> rules = new ArrayList<>();

    static {
        register(new Rule001Location());
    }

    public static void register(CheckRule rule) {
        rules.add(rule);
    }

    public static ValidationResult validate(String path, String content) {
        List<String> allErrors = new ArrayList<>();
        for (CheckRule rule : rules) {
            ValidationResult result = rule.validate(path, content);
            if (!result.valid()) {
                allErrors.addAll(result.errors());
            }
        }
        return new ValidationResult(allErrors.isEmpty(), allErrors);
    }
}
