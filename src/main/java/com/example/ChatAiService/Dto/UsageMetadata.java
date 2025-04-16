package com.example.ChatAiService.Dto;

import java.util.List;

public class UsageMetadata {
    private Integer promptTokenCount;
    private Integer candidatesTokenCount;
    private Integer totalTokenCount;
    private List<TokensDetail> promptTokensDetails;
    private List<TokensDetail> candidatesTokensDetails;

    // Constructors
    public UsageMetadata() {
    }

    public UsageMetadata(Integer promptTokenCount, Integer candidatesTokenCount, Integer totalTokenCount, List<TokensDetail> promptTokensDetails, List<TokensDetail> candidatesTokensDetails) {
        this.promptTokenCount = promptTokenCount;
        this.candidatesTokenCount = candidatesTokenCount;
        this.totalTokenCount = totalTokenCount;
        this.promptTokensDetails = promptTokensDetails;
        this.candidatesTokensDetails = candidatesTokensDetails;
    }

    // Getters and Setters
    public Integer getPromptTokenCount() {
        return promptTokenCount;
    }

    public void setPromptTokenCount(Integer promptTokenCount) {
        this.promptTokenCount = promptTokenCount;
    }

    public Integer getCandidatesTokenCount() {
        return candidatesTokenCount;
    }

    public void setCandidatesTokenCount(Integer candidatesTokenCount) {
        this.candidatesTokenCount = candidatesTokenCount;
    }

    public Integer getTotalTokenCount() {
        return totalTokenCount;
    }

    public void setTotalTokenCount(Integer totalTokenCount) {
        this.totalTokenCount = totalTokenCount;
    }

    public List<TokensDetail> getPromptTokensDetails() {
        return promptTokensDetails;
    }

    public void setPromptTokensDetails(List<TokensDetail> promptTokensDetails) {
        this.promptTokensDetails = promptTokensDetails;
    }

    public List<TokensDetail> getCandidatesTokensDetails() {
        return candidatesTokensDetails;
    }

    public void setCandidatesTokensDetails(List<TokensDetail> candidatesTokensDetails) {
        this.candidatesTokensDetails = candidatesTokensDetails;
    }

    @Override
    public String toString() {
        return "UsageMetadata{" +
                "promptTokenCount=" + promptTokenCount +
                ", candidatesTokenCount=" + candidatesTokenCount +
                ", totalTokenCount=" + totalTokenCount +
                ", promptTokensDetails=" + promptTokensDetails +
                ", candidatesTokensDetails=" + candidatesTokensDetails +
                '}';
    }
}
