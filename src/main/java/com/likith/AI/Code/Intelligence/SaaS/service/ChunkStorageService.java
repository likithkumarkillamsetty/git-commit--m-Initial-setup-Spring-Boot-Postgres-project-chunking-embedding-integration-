package com.likith.AI.Code.Intelligence.SaaS.service;

import com.likith.AI.Code.Intelligence.SaaS.entity.CodeChunkEntity;
import com.likith.AI.Code.Intelligence.SaaS.model.CodeChunk;
import com.likith.AI.Code.Intelligence.SaaS.repository.CodeChunkRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChunkStorageService {

    private final CodeChunkRepository repository;
    private final EmbeddingService embeddingService;

    @PersistenceContext
    private EntityManager entityManager;

    public ChunkStorageService(CodeChunkRepository repository,
                               EmbeddingService embeddingService) {
        this.repository = repository;
        this.embeddingService = embeddingService;
    }

    @Transactional
    public void storeChunks(Long projectId, List<CodeChunk> chunks) {

        for (CodeChunk chunk : chunks) {

            CodeChunkEntity entity = new CodeChunkEntity();
            entity.setProjectId(projectId);
            entity.setFilePath(chunk.getFilePath());
            entity.setContent(chunk.getChunkContent());

            repository.save(entity);

            float[] embedding = embeddingService.generateEmbedding(chunk.getChunkContent());

            String vectorString = toPgVectorString(embedding);

            entityManager.createNativeQuery("""
                    UPDATE code_chunks
                    SET embedding = (:embedding)::vector
                    WHERE id = :id
                    """)
                    .setParameter("embedding", vectorString)
                    .setParameter("id", entity.getId())
                    .executeUpdate();
        }
    }

    private String toPgVectorString(float[] embedding) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < embedding.length; i++) {
            sb.append(embedding[i]);
            if (i < embedding.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}