package com.nt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.modal.InterviewQuestion;
import com.nt.modal.QuestionsAnswersDTO;

@Component
public class AIService {

    @Autowired
    private RestTemplate restTemplate;

   private static final String TOKEN = "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";
    
    
    private static final String API_URL =
    		  "https://api.groq.com/openai/v1/chat/completions";

    public List<InterviewQuestion> generateQuestions(String prompt) {
    	
    	
    	
    	
    	//prompt 
    	String goodPrompt="You are an expert interview question generator.  Given the following resume text, generate 3 interview questions based on it. Each question should be returned in  *JSON format only**, with the following fields:\r\n"
    			+ "- id (number)\r\n"
    			+ "- questiontext (string)\r\n"
    			+ "- category (hr or technical)\r\n"
    			
    			+ "- difficulty (easy, medium, or hard) \r\n"
    			+ ""+prompt+"Only return a JSON array of questions. No explanation or additional text.";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);

        Map<String, Object> requestBody = Map.of(
            "model", "llama-3.1-8b-instant",
            "messages", List.of(
                Map.of("role", "user", "content", goodPrompt)
            ),
            "temperature", 0.7,
            "max_tokens", 1024
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL", requestEntity, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            String content = root.path("choices").get(0).path("message").path("content").asText();

            // Extract only the JSON part (first '[' to last ']')
            int start = content.indexOf('[');
            int end = content.lastIndexOf(']');
            if (start == -1 || end == -1) {
                throw new RuntimeException("JSON array not found in response content.");
            }

            String jsonArray = content.substring(start, end + 1);

            // Parse the array string into list of objects
            return mapper.readValue(
                jsonArray,
                mapper.getTypeFactory().constructCollectionType(List.class, InterviewQuestion.class)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    // ✅ SINGLE AI CALL FOR ALL QUESTIONS
    public JsonNode getBulkFeedbackFromAI(List<QuestionsAnswersDTO> responses) {

        StringBuilder prompt = new StringBuilder("""
        You are a senior technical interviewer.

        Evaluate the following interview answers.
        Return ONLY valid JSON in the exact format below.
        Do NOT add markdown, text, or explanation.

        [
          {
            "feedback": "2-3 lines constructive feedback",
            "score": 0-10
          }
        ]
        """);

        for (int i = 0; i < responses.size(); i++) {
            prompt.append("""
            
            Question %d:
            %s
            
            Candidate Answer:
            %s
            """.formatted(
                    i + 1,
                    responses.get(i).getQuestionText(),
                    responses.get(i).getAnswer()
            ));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(TOKEN);

        Map<String, Object> body = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt.toString())
                ),
                "temperature", 0.3,
                "max_tokens", 900
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(API_URL, request, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // 🔥 SAFELY EXTRACT JSON ARRAY
            int start = content.indexOf('[');
            int end = content.lastIndexOf(']');

            if (start == -1 || end == -1) {
                throw new RuntimeException("Invalid AI JSON response");
            }

            return mapper.readTree(content.substring(start, end + 1));

        } catch (Exception e) {
            throw new RuntimeException("AI feedback parsing failed", e);
        }
    }

}
