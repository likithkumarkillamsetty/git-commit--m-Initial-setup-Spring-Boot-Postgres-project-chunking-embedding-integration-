package com.likith.AI.Code.Intelligence.SaaS.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public String toPgVectorString(float[] vector) {
        return "[" +
                IntStream.range(0, vector.length)
                        .mapToObj(i -> String.valueOf(vector[i]))
                        .collect(Collectors.joining(",")) +
                "]";
    }

    public String generateAnswer(String prompt) {

        int contextStart = prompt.indexOf("Context:");
        int questionStart = prompt.indexOf("Question:");

        if (contextStart == -1 || questionStart == -1) {
            return "Unable to process question.";
        }

        String context = prompt.substring(contextStart + 8, questionStart).trim();
        String question = prompt.substring(questionStart + 9).trim();

        return """
            Answer (simulated AI response):

            Based on the retrieved repository code, the system appears to handle this as follows:

            %s

            Note: This is a mock AI response generated from retrieved context.
            """.formatted(context.substring(0, Math.min(400, context.length())));
    }
}