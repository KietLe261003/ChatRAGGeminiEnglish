package com.example.ChatAiService.Dto;

public class TokensDetail {
    private String modality;
    private Integer tokenCount;

    // Constructors
    public TokensDetail() {
    }

    public TokensDetail(String modality, Integer tokenCount) {
        this.modality = modality;
        this.tokenCount = tokenCount;
    }

    // Getters and Setters
    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }

    @Override
    public String toString() {
        return "TokensDetail{" +
                "modality='" + modality + '\'' +
                ", tokenCount=" + tokenCount +
                '}';
    }
}
