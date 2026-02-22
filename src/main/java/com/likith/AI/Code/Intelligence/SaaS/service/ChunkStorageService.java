package com.likith.AI.Code.Intelligence.SaaS.service;

import com.likith.AI.Code.Intelligence.SaaS.entity.CodeChunkEntity;
import com.likith.AI.Code.Intelligence.SaaS.model.CodeChunk;
import com.likith.AI.Code.Intelligence.SaaS.repository.CodeChunkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChunkStorageService {

    private final CodeChunkRepository repository;
    private final EmbeddingService embeddingService;

    public ChunkStorageService(CodeChunkRepository repository,
                               EmbeddingService embeddingService) {
        this.repository = repository;
        this.embeddingService = embeddingService;
    }

    public void storeChunks(Long projectId, List<CodeChunk> chunks) {

        for (CodeChunk chunk : chunks) {

            CodeChunkEntity entity = new CodeChunkEntity();
            entity.setProjectId(projectId);
            entity.setFilePath(chunk.getFilePath());
            entity.setContent(chunk.getChunkContent());

            float[] embedding =
                    embeddingService.generateEmbedding(chunk.getChunkContent());

            entity.setEmbedding(embedding);

            repository.save(entity);
        }
    }
}