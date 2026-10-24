package com.career.guidance.service.rag;

import java.util.List;

public interface VectorStore {
    void store(DocumentChunk chunk);
    void storeAll(List<DocumentChunk> chunks);
    List<DocumentChunk> searchSimilar(String query, int topK);
    void clear();
}
