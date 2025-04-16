package com.example.ChatAiService.Config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Configuration
public class Config {

    @Value("classpath:/CourseData.txt")
    private Resource courseDataResource;

    @Value("${vectorstore.filepath:./vector_store.json}")
    private String vectorStorePath;

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = new File(vectorStorePath);

        if (vectorStoreFile.exists()) {
            System.out.println("✅ Vector store file found — loading existing vector store...");
            vectorStore.load(vectorStoreFile);
        } else {
            System.out.println("🆕 Vector store not found — creating new vector store...");

            List<Document> documents = readDocumentsFromCourseData();
            vectorStore.add(documents);

            vectorStore.save(vectorStoreFile);
            System.out.println("💾 New vector store saved to: " + vectorStorePath);
        }

        return vectorStore;
    }

    private List<Document> readDocumentsFromCourseData() {
        List<Document> documents = new ArrayList<>();
        String content;

        try {
            content = new String(courseDataResource.getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to read CourseData.txt", e);
        }

        String[] courseBlocks = content.split("\n\n");
        for (String block : courseBlocks) {
            documents.add(parseCourseBlockToDocument(block));
        }

        return documents;
    }

    private Document parseCourseBlockToDocument(String block) {
        Document doc = new Document(block.trim());
        Map<String, String> metadata = new HashMap<>();

        String[] lines = block.split("\n");
        for (String line : lines) {
            if (line.startsWith("ID: ")) {
                metadata.put("course_id", extractValue(line));
            } else if (line.startsWith("Tên khóa học: ")) {
                metadata.put("course_name", extractValue(line));
            } else if (line.startsWith("Kỹ năng có được: ")) {
                metadata.put("skills", extractValue(line));
            } else if (line.startsWith("Giáo viên: ")) {
                metadata.put("teacher", extractValue(line));
            } else if (line.startsWith("Thời gian: ")) {
                metadata.put("duration", extractValue(line));
            } else if (line.startsWith("Số lượng học viên: ")) {
                metadata.put("student_count", extractValue(line));
            } else if (line.startsWith("Mô tả: ")) {
                metadata.put("description", extractValue(line));
            }
        }

        doc.getMetadata().putAll(metadata);
        return doc;
    }

    private String extractValue(String line) {
        return line.substring(line.indexOf(":") + 1).trim();
    }
}
