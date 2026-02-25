package com.likith.AI.Code.Intelligence.SaaS.service;

import com.likith.AI.Code.Intelligence.SaaS.dto.SearchResult;
import com.likith.AI.Code.Intelligence.SaaS.entity.CodeChunkEntity;
import com.likith.AI.Code.Intelligence.SaaS.repository.CodeChunkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final CodeChunkRepository repository;
    private final EmbeddingService embeddingService;
    private final OllamaChatService chatService;

    public SearchService(CodeChunkRepository repository,
                         EmbeddingService embeddingService,
                         OllamaChatService chatService) {
        this.repository = repository;
        this.embeddingService = embeddingService;
        this.chatService = chatService;
    }

    public List<SearchResult> search(Long projectId, String query) {

        float[] embedding = embeddingService.getEmbedding(query);

        List<CodeChunkEntity> chunks =
                repository.searchSimilarChunks(projectId, embedding, 5);

        return chunks.stream()
                .map(chunk -> new SearchResult(
                        chunk.getFilePath(),
                        chunk.getContent(),
                        0.0   // we are not returning similarity score for now
                ))
                .toList();
    }

    // 🤖 Full RAG
    public String askQuestion(Long projectId, String question) {

        float[] queryEmbedding = embeddingService.getEmbedding(question);

        List<CodeChunkEntity> chunks =
                repository.searchSimilarChunks(projectId, queryEmbedding, 5);

        if (chunks.isEmpty()) {
            return "No relevant context found.";
        }

        String context = chunks.stream()
                .map(CodeChunkEntity::getContent)
                .collect(Collectors.joining("\n\n"));

        String prompt = """
                You are a code assistant.
                Answer using ONLY the provided context.

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        return chatService.generateAnswer(prompt);
    }
}