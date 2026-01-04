package org.fpf.governance.vault;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileVaultProvider implements VaultProvider {
    private final Path root;

    public FileVaultProvider(String rootPath) {
        this.root = Paths.get(rootPath).toAbsolutePath();
        if (!Files.exists(this.root) || !Files.isDirectory(this.root)) {
            throw new IllegalArgumentException("Vault root does not exist or is not a directory: " + rootPath);
        }
    }

    @Override
    public boolean exists(String relativePath) {
        Path path = root.resolve(relativePath).normalize();
        // Security check: prevent directory traversal out of vault
        if (!path.startsWith(root)) {
            throw new IllegalArgumentException("Path traversal attempt: " + relativePath);
        }
        return Files.exists(path);
    }

    @Override
    public String readContent(String relativePath) {
        Path path = root.resolve(relativePath).normalize();
        if (!path.startsWith(root)) {
            throw new IllegalArgumentException("Path traversal attempt: " + relativePath);
        }
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + relativePath, e);
        }
    }

    @Override
    public List<String> findFiles(String directory, String regexPattern) {
        Path contextDir = root.resolve(directory).normalize();
        if (!contextDir.startsWith(root)) {
            throw new IllegalArgumentException("Path traversal attempt: " + directory);
        }
        if (!Files.exists(contextDir) || !Files.isDirectory(contextDir)) {
            return Collections.emptyList();
        }

        try (Stream<Path> stream = Files.walk(contextDir, 1)) {
            return stream
                    .filter(p -> Files.isRegularFile(p))
                    .map(p -> p.getFileName().toString())
                    .filter(name -> name.matches(regexPattern))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to walk directory: " + directory, e);
        }
    }
}
