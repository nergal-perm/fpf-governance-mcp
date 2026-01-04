package org.fpf.governance.checks;

import org.fpf.governance.vault.VaultProvider;

public interface CheckRule {
    ValidationResult validate(String path, String content, VaultProvider vault);
}
