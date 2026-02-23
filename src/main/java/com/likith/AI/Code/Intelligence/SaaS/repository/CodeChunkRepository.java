package com.likith.AI.Code.Intelligence.SaaS.repository;

import com.likith.AI.Code.Intelligence.SaaS.entity.CodeChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeChunkRepository extends JpaRepository<CodeChunkEntity, Long> {

    @Query(value = """
        SELECT id,
               project_id,
               file_path,
               content,
               embedding <-> (:embedding)::vector AS score
        FROM code_chunks
        WHERE project_id = :projectId
        ORDER BY embedding <-> (:embedding)::vector
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> searchSimilarChunksRaw(
            @Param("projectId") Long projectId,
            @Param("embedding") String embedding,
            @Param("limit") int limit
    );
}