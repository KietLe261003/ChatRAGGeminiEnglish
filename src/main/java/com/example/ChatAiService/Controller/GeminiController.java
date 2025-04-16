package com.example.ChatAiService.Controller;

import com.example.ChatAiService.Dto.Candidate;
import com.example.ChatAiService.Dto.GeminiResponse;
import com.example.ChatAiService.Service.QnAService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ai/gemini")
public class GeminiController {
    @Autowired
    private QnAService qnAService;

    private final VectorStore vectorStore;

    public GeminiController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @GetMapping("/generate/{message}")
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
            Dựa trên danh sách các khóa học tiếng Anh sau đây:
            
            %s
            
            Hãy trả lời câu hỏi của người dùng một cách chính xác và ngắn gọn:
            
            "%s"
            
            **Hướng dẫn trả lời:**
            
            1. **Tìm kiếm khóa học phù hợp nhất:** Phân tích câu hỏi của người dùng để xác định khóa học phù hợp nhất dựa trên các tiêu chí như trình độ (ưu tiên "Cơ bản" cho người mới bắt đầu), mục tiêu học tập (giao tiếp, luyện thi, du lịch, công việc, phát âm), hình thức học (offline, online), và các yêu cầu khác nếu có.
            
            2. **Trích xuất thông tin liên quan:** Khi tìm được khóa học phù hợp, hãy trích xuất các thông tin quan trọng sau:
                - Tên khóa học
                - Thời lượng
                - Lịch học
                - Học phí
                - Hình thức học
                - Đặc điểm nổi bật
                - Ưu đãi hiện có
            
            3. **Soạn câu trả lời:** Dựa trên thông tin đã trích xuất, hãy trả lời người dùng bằng tiếng Việt một cách rõ ràng, trực tiếp và cung cấp thông tin hữu ích nhất.
            
            4. **Xử lý trường hợp không có khóa học phù hợp:** Nếu không tìm thấy khóa học nào hoàn toàn phù hợp với yêu cầu của người dùng, hãy trả lời một cách lịch sự: "Hiện tại trung tâm chưa có khóa học phù hợp với nhu cầu này."
            
            **Quy tắc quan trọng:**
            
            - **Chỉ sử dụng thông tin từ danh sách khóa học được cung cấp.** Tuyệt đối không suy diễn, thêm thông tin không có hoặc tạo ra các khóa học mới.
            - **Ưu tiên khóa học có trình độ "Cơ bản" nếu người dùng là người mới bắt đầu hoặc không đề cập đến trình độ cụ thể.**
            - **Trả lời ngắn gọn và đi vào trọng tâm.** Tránh những câu trả lời dài dòng hoặc không liên quan.
            - **Luôn sử dụng tiếng Việt.**
            
            """.formatted(context, message);


            GeminiResponse response = qnAService.getAnswer(promptText);

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                Candidate firstCandidate = response.getCandidates().get(0);
                if (firstCandidate != null && firstCandidate.getContent() != null && firstCandidate.getContent().getParts() != null && !firstCandidate.getContent().getParts().isEmpty()) {
                    // Lấy danh sách các đối tượng Part
                    List<com.example.ChatAiService.Dto.Part> partsList = firstCandidate.getContent().getParts();
                    // Nếu bạn chỉ muốn lấy text của phần tử đầu tiên trong danh sách parts
                    if (!partsList.isEmpty()) {
                        String firstPartText = partsList.get(0).getText();
                        return firstPartText;
                    } else {
                        return "Không có phần nào (parts) trong phản hồi.";
                    }
                } else {
                    return "Không có nội dung (content) hoặc phần (parts) trong phản hồi.";
                }
            } else {
                return "Không tìm thấy thông tin phản hồi hợp lệ.";
            }

        } catch (Exception e) {
            return "Đã xảy ra lỗi khi xử lý yêu cầu: " + e.getMessage();
        }
    }

//    @PostMapping("/ask")
//    public ResponseEntity<String> askQuestion(@RequestBody Map<String, String> payload){
//        String question = payload.get("question");
//        String answer = qnAService.getAnswer(question);
//        return ResponseEntity.ok(answer);
//    }
}
