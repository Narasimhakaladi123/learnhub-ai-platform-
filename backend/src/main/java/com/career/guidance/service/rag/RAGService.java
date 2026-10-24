package com.career.guidance.service.rag;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RAGService {

    private static final Logger log = LoggerFactory.getLogger(RAGService.class);
    private final VectorStore vectorStore;

    public RAGService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void initKnowledgeBase() {
        log.info("Initializing RAG Career Knowledge Base with career guides, industry skill trees, and interview frameworks...");

        List<DocumentChunk> initialKnowledgeChunks = List.of(
            new DocumentChunk("rag-doc-1",
                "Java Backend Developer Roadmap: Requires proficiency in Java 17+, OOP principles, Spring Boot, Spring Data JPA, Spring Security, Relational Databases (MySQL/PostgreSQL), Docker, and Microservices architecture. Recommended projects: Multi-tier REST API, E-commerce Order Management System, Event-driven Kafka service. Key interview topics: Garbage collection, concurrency, Spring Bean lifecycle, ACID properties, index optimization.",
                "Engineering Career Guide 2025",
                Map.of("category", "BACKEND", "career", "Java Backend Developer")),

            new DocumentChunk("rag-doc-2",
                "Full Stack Developer Blueprint: Encompasses both client-side and server-side engineering. Core technologies: TypeScript, Angular/React, Node.js/Java, RESTful and GraphQL APIs, State management (NgRx/Redux), Responsive UI (Tailwind CSS), CI/CD pipelines, and Cloud deployment on AWS/GCP. Key focus: End-to-end user flows, JWT security, and API performance.",
                "Full Stack Engineering Handbook",
                Map.of("category", "FULL_STACK", "career", "Full Stack Developer")),

            new DocumentChunk("rag-doc-3",
                "Cloud & DevOps Engineer Roadmap: Core skills: Linux administration, Docker containerization, Kubernetes orchestration, Infrastructure as Code (Terraform), CI/CD (GitHub Actions/Jenkins), Cloud providers (AWS, Azure), Observability (Prometheus, Grafana), and Network security.",
                "DevOps & SRE Practice Guide",
                Map.of("category", "DEVOPS", "career", "DevOps Engineer")),

            new DocumentChunk("rag-doc-4",
                "Data Engineer & AI/ML Specialist Guide: Key competencies: Python, SQL, Apache Spark, Data modeling, ETL pipelines, Vector databases, TensorFlow/PyTorch, LLM prompting & fine-tuning, RAG architecture, and ML model serving with FastAPI/Docker.",
                "AI/ML Industry Standard 2025",
                Map.of("category", "AI_ML", "career", "AI/ML Engineer")),

            new DocumentChunk("rag-doc-5",
                "Cybersecurity & QA Automation Guide: Penetration testing fundamentals, OWASP Top 10, Network vulnerability scanning, Selenium/Playwright automation, JUnit/TestNG, Postman API testing, and DevSecOps compliance.",
                "Quality & Security Standards",
                Map.of("category", "CYBERSECURITY", "career", "Cybersecurity Engineer"))
        );

        vectorStore.storeAll(initialKnowledgeChunks);
        log.info("RAG Knowledge Base initialized with {} canonical domain chunks.", initialKnowledgeChunks.size());
    }

    public List<DocumentChunk> retrieveRelevantContext(String query, int topK) {
        return vectorStore.searchSimilar(query, topK);
    }

    public String buildAugmentedContext(String query, int topK) {
        List<DocumentChunk> relevantChunks = retrieveRelevantContext(query, topK);
        if (relevantChunks.isEmpty()) {
            return "";
        }

        StringBuilder contextBuilder = new StringBuilder("\n--- RETRIEVED DOMAIN KNOWLEDGE BASE CONTEXT ---\n");
        for (DocumentChunk chunk : relevantChunks) {
            contextBuilder.append("[").append(chunk.getSource()).append("]: ")
                .append(chunk.getContent()).append("\n\n");
        }
        contextBuilder.append("--- END OF DOMAIN KNOWLEDGE BASE ---\n");
        return contextBuilder.toString();
    }
}
