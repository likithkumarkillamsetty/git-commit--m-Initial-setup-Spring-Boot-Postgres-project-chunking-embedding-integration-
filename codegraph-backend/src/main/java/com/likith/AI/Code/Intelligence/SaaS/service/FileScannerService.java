package com.likith.AI.Code.Intelligence.SaaS.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileScannerService {

    private static final List<String> SUPPORTED_EXTENSIONS = List.of(
            // JVM
            ".java", ".kt", ".scala", ".groovy",
            // Web frontend
            ".tsx", ".ts", ".jsx", ".js", ".css", ".scss", ".html", ".vue", ".svelte",
            // Python
            ".py",
            // Go
            ".go",
            // Rust
            ".rs",
            // Ruby
            ".rb",
            // C/C++
            ".c", ".cpp", ".h", ".hpp",
            // C#
            ".cs",
            // PHP
            ".php",
            // Shell
            ".sh",
            // Config/Data
            ".json", ".yml", ".yaml", ".xml", ".toml",
            // Docs/DB
            ".md", ".txt", ".sql", ".properties"
    );

    private static final List<String> IGNORED_DIRS = List.of(
            ".git", "node_modules", "target", "build", ".idea", "__pycache__",
            "dist", ".vite", "coverage", ".next", "out", "bin", "obj",
            "vendor", "venv", ".venv", ".gradle", ".mvn"
    );

    private static final long MAX_FILE_SIZE_BYTES = 50_000; // 50KB

    public List<File> scanJavaFiles(String projectPath) {
        List<File> files = new ArrayList<>();
        File root = new File(projectPath);
        scanRecursively(root, files);
        return files;
    }

    private void scanRecursively(File file, List<File> files) {
        if (file == null || !file.exists()) return;

        if (file.isDirectory()) {
            if (IGNORED_DIRS.contains(file.getName())) return;
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    scanRecursively(child, files);
                }
            }
        } else {
            // Skip files larger than 50KB — likely generated or minified
            if (file.length() > MAX_FILE_SIZE_BYTES) return;

            String name = file.getName();
            for (String ext : SUPPORTED_EXTENSIONS) {
                if (name.endsWith(ext)) {
                    files.add(file);
                    break;
                }
            }
        }
    }
}