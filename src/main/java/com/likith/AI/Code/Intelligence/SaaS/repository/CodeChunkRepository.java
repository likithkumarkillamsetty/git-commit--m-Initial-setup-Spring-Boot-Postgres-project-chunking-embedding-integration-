package com.likith.AI.Code.Intelligence.SaaS.repository;

import com.likith.AI.Code.Intelligence.SaaS.entity.CodeChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeChunkRepository extends JpaRepository<CodeChunkEntity, Long> {

    @Query(value = """
            SELECT * FROM code_chunks
            WHERE project_id = :projectId
            ORDER BY embedding <-> CAST(:embedding AS vector)
            LIMIT :limit
            """, nativeQuery = true)
    List<CodeChunkEntity> searchSimilarChunks(
            @Param("projectId") Long projectId,
            @Param("embedding") float[] embedding,
            @Param("limit") int limit);
}