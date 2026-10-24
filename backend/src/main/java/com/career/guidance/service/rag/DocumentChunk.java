package com.career.guidance.service.rag;

import java.util.Map;

public class DocumentChunk {
    private String id;
    private String content;
    private String source;
    private Map<String, Object> metadata;
    private double[] embedding;

    public DocumentChunk() {
    }

    public DocumentChunk(String id, String content, String source, Map<String, Object> metadata) {
        this.id = id;
        this.content = content;
        this.source = source;
        this.metadata = metadata;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public double[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(double[] embedding) {
        this.embedding = embedding;
    }
}
