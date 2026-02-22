package com.likith.AI.Code.Intelligence.SaaS.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmbeddingService {

    private static final int DIMENSION = 1536;
    private final Random random = new Random();

    public float[] generateEmbedding(String text) {
        random.setSeed(text.hashCode());
        float[] vector = new float[DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            vector[i] = random.nextFloat();
        }
        return vector;
    }
}