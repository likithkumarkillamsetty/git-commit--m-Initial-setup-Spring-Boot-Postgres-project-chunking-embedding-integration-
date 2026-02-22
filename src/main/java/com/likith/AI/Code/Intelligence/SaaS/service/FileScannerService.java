package com.likith.AI.Code.Intelligence.SaaS.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileScannerService {

    public List<File> scanJavaFiles(String projectPath) {
        List<File> javaFiles = new ArrayList<>();
        File root = new File(projectPath);

        scanRecursively(root, javaFiles);

        return javaFiles;
    }

    private void scanRecursively(File file, List<File> javaFiles) {

        if (file == null || !file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    scanRecursively(child, javaFiles);
                }
            }
        } else {
            if (file.getName().endsWith(".java")) {
                javaFiles.add(file);
            }
        }
    }
}