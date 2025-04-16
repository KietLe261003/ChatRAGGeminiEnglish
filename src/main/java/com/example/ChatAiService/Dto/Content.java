package com.example.ChatAiService.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Content {
    private List<Part> parts;

    // Constructors
    public Content() {
    }

    public Content(List<Part> parts) {
        this.parts = parts;
    }

    // Getters and Setters
    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    @Override
    public String toString() {
        return "Content{" +
                "parts=" + parts +
                '}';
    }
}
