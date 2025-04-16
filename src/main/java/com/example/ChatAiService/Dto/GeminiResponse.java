package com.example.ChatAiService.Dto;

import java.util.List;

public class GeminiResponse {
    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;
    private String modelVersion;

    // Constructors (no-arg và with arguments)
    public GeminiResponse() {
    }

    public GeminiResponse(List<Candidate> candidates, UsageMetadata usageMetadata, String modelVersion) {
        this.candidates = candidates;
        this.usageMetadata = usageMetadata;
        this.modelVersion = modelVersion;
    }

    // Getters and Setters
    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public UsageMetadata getUsageMetadata() {
        return usageMetadata;
    }

    public void setUsageMetadata(UsageMetadata usageMetadata) {
        this.usageMetadata = usageMetadata;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    @Override
    public String toString() {
        return "GeminiResponse{" +
                "candidates=" + candidates +
                ", usageMetadata=" + usageMetadata +
                ", modelVersion='" + modelVersion + '\'' +
                '}';
    }
}

