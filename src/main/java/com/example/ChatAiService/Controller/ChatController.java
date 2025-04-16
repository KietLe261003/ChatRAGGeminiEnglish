package com.example.ChatAiService.Controller;


import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final OllamaChatModel chatModel;
    private final VectorStore vectorStore; // Thêm VectorStore
    private final EmbeddingModel embeddingClient;


    @Autowired
    public ChatController(OllamaChatModel chatModel, VectorStore vectorStore,EmbeddingModel embeddingClient) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.embeddingClient = embeddingClient;
    }

    @GetMapping("/ai/generate/{message}")
    public String generate(@PathVariable("message") String message) {
        try {
            // Tìm tài liệu liên quan
            List<Document> relevantDocs = vectorStore.similaritySearch(message).stream().limit(5).collect(Collectors.toList());
            if (relevantDocs.isEmpty()) {
                return "Hiện chưa có khóa học phù hợp.";
            }

            String context = relevantDocs.stream()
                    .map(Document::getText)
                    .reduce("", (a, b) -> a + "\n" + b);

            String promptText = """
            Dựa vào danh sách khóa học đang có:
            
            %s
            
            Hãy trả lời câu hỏi:
            
            "%s"
            
            **Yêu cầu:**
            - Hãy đọc kỹ các thông tin trên.
            - Sử dụng tiếng Việt cho câu trả lời
            - Nếu không có, trả lời: "Hiện tại trung tâm chưa có khóa học phù hợp với nhu cầu này."
            - **Tuyệt đối không suy diễn hay tự thêm thông tin không có trong danh sách**.
            
            **Lưu ý:** Đối với người mới bắt đầu, trình độ **Cơ bản** là lựa chọn chính.
            """.formatted(context, message);



            String response = this.chatModel.call(promptText);
            String formattedResponse = response.replaceAll("<think>.*?</think>", "").trim();

            return formattedResponse;
        } catch (Exception e) {
            return "Đã xảy ra lỗi khi xử lý yêu cầu: " + e.getMessage();
        }
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        // Tương tự như trên, nhưng với streaming
        List<Document> relevantDocs = vectorStore.similaritySearch(message);
        String context = relevantDocs.stream().map(Document::getText).reduce("", (a, b) -> a + "\n" + b);
        String promptText = "Dựa trên thông tin sau: \"" + context + "\", hãy trả lời câu hỏi: \"" + message + "\"";

        Prompt prompt = new Prompt(new UserMessage(promptText));
        return this.chatModel.stream(prompt);
    }


}