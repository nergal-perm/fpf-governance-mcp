package org.fpf.governance.vault;

import java.util.List;

public interface VaultProvider {
    boolean exists(String relativePath);

    String readContent(String relativePath);

    // For RULE-202: Find files of a certain type in a context (directory)
    List<String> findFiles(String directory, String regexPattern);
}
