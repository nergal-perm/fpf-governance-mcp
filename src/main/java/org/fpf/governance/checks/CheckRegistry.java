package org.fpf.governance.checks;

import java.util.ArrayList;
import java.util.List;

public class CheckRegistry {
    private static final List<CheckRule> rules = new ArrayList<>();

    static {
        register(new Rule001Location());
        register(new Rule002Naming());
        register(new Rule1xxLinkage());
        register(new Rule201Assurance());
        register(new Rule202ActiveLimit());
    }

    public static void register(CheckRule rule) {
        rules.add(rule);
    }

    public static ValidationResult validate(String path, String content, org.fpf.governance.vault.VaultProvider vault) {
        List<String> allErrors = new ArrayList<>();
        for (CheckRule rule : rules) {
            ValidationResult result = rule.validate(path, content, vault);
            if (!result.valid()) {
                allErrors.addAll(result.errors());
            }
        }
        return new ValidationResult(allErrors.isEmpty(), allErrors);
    }
}
