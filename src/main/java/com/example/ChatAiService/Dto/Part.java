package com.example.ChatAiService.Dto;

public class Part {
    private String text;

    // Constructors
    public Part() {
    }

    public Part(String text) {
        this.text = text;
    }

    // Getter and Setter
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Part{" +
                "text='" + text + '\'' +
                '}';
    }
}
