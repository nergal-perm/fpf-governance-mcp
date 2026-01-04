package org.fpf.governance.checks;

public interface CheckRule {
    ValidationResult validate(String path, String content);
}
