package com.likith.AI.Code.Intelligence.SaaS.dto;

public record SearchResult(
        String filePath,
        String snippet,
        double score
) {}