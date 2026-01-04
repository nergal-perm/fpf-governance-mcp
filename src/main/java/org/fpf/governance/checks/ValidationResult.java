package org.fpf.governance.checks;

import java.util.List;

public record ValidationResult(boolean valid, List<String> errors) {}
