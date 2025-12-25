package com.nt.controller;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.nt.config.SecurityConfig;
import com.nt.modal.InterviewFeedBackRequest;
import com.nt.modal.InterviewFeedBackResponse;
import com.nt.modal.QuestionsAnswersDTO;
import com.nt.service.AIService;

@RestController
@RequestMapping("/api/interview")

public class InterviewFeedbackController {

    

	@Autowired
    private AIService aiService;


    
    
    @PostMapping("/submit")
    public JsonNode submitInterview(
            @RequestBody InterviewFeedBackRequest request) {

        return aiService.getBulkFeedbackFromAI(request.getResponses());
    }
}
