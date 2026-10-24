package com.career.guidance.service.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class InMemoryVectorStore implements VectorStore {

    private static final Logger log = LoggerFactory.getLogger(InMemoryVectorStore.class);
    private static final int VECTOR_DIMENSION = 128;
    private final List<DocumentChunk> store = new CopyOnWriteArrayList<>();

    @Override
    public void store(DocumentChunk chunk) {
        if (chunk.getEmbedding() == null) {
            chunk.setEmbedding(generateEmbedding(chunk.getContent()));
        }
        store.add(chunk);
    }

    @Override
    public void storeAll(List<DocumentChunk> chunks) {
        for (DocumentChunk chunk : chunks) {
            store(chunk);
        }
    }

    @Override
    public List<DocumentChunk> searchSimilar(String query, int topK) {
        if (store.isEmpty() || query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        double[] queryVector = generateEmbedding(query);

        return store.stream()
            .map(chunk -> new AbstractMap.SimpleEntry<>(chunk, cosineSimilarity(queryVector, chunk.getEmbedding())))
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .filter(e -> e.getValue() > 0.05) // similarity threshold
            .limit(topK)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        store.clear();
    }

    /**
     * Generates a lightweight, deterministic hashed semantic vector for query/chunk content.
     */
    private double[] generateEmbedding(String text) {
        double[] vector = new double[VECTOR_DIMENSION];
        if (text == null) return vector;

        String[] tokens = text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ").split("\\s+");
        for (String token : tokens) {
            if (token.length() > 2) {
                int hash = Math.abs(token.hashCode()) % VECTOR_DIMENSION;
                vector[hash] += 1.0;
            }
        }

        // Normalize vector to unit length
        double norm = 0.0;
        for (double v : vector) {
            norm += v * v;
        }
        norm = Math.sqrt(norm);
        if (norm > 0) {
            for (int i = 0; i < VECTOR_DIMENSION; i++) {
                vector[i] /= norm;
            }
        }
        return vector;
    }

    private double cosineSimilarity(double[] v1, double[] v2) {
        if (v1 == null || v2 == null || v1.length != v2.length) return 0.0;
        double dot = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        for (int i = 0; i < v1.length; i++) {
            dot += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }
        double denominator = Math.sqrt(norm1) * Math.sqrt(norm2);
        return denominator == 0 ? 0.0 : dot / denominator;
    }
}
