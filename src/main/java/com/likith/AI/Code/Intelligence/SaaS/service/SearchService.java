package com.likith.AI.Code.Intelligence.SaaS.service;

import com.likith.AI.Code.Intelligence.SaaS.dto.SearchResult;
import com.likith.AI.Code.Intelligence.SaaS.repository.CodeChunkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final CodeChunkRepository repository;
    private final EmbeddingService embeddingService;

    public SearchService(CodeChunkRepository repository,
                         EmbeddingService embeddingService) {
        this.repository = repository;
        this.embeddingService = embeddingService;
    }

    public List<SearchResult> search(Long projectId, String query) {

        float[] embedding = embeddingService.generateEmbedding(query);
        String vectorString = toPgVectorString(embedding);

        List<Object[]> results =
                repository.searchSimilarChunksRaw(projectId, vectorString, 5);

        return results.stream()
                .map(row -> new SearchResult(
                        (String) row[2],
                        trimContent((String) row[3]),
                        ((Number) row[4]).doubleValue()
                ))
                .toList();
    }

    private String trimContent(String content) {
        if (content.length() > 500) {
            return content.substring(0, 500);
        }
        return content;
    }

    private String toPgVectorString(float[] embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.length; i++) {
            sb.append(embedding[i]);
            if (i < embedding.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public String askQuestion(Long projectId, String question) {

        List<SearchResult> results = search(projectId, question);

        StringBuilder context = new StringBuilder();

        for (SearchResult result : results) {
            context.append(result.snippet()).append("\n\n");
        }

        String prompt = """
            You are a code assistant.
            Answer the question using ONLY the provided context.

            Context:
            %s

            Question:
            %s
            """.formatted(context.toString(), question);

        return embeddingService.generateAnswer(prompt);
    }
}