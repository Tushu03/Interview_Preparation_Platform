package com.nt.controller;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nt.modal.InterviewQuestion;
import com.nt.service.AIService;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


import io.swagger.v3.oas.annotations.Operation;

@RestController
public class GenAiController 
{
    @Autowired
    private AIService service;

    @PostMapping("/api/questions/generate")
    @Operation(summary = "Generate questions from uploaded resume file")
    public List<InterviewQuestion> generateQuestionsFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        
        String resumeText = extractTextFromFile(file);

       
        return service.generateQuestions(resumeText);
    }

    private String extractTextFromFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new IOException("File name is invalid.");
        }

        if (fileName.endsWith(".pdf")) {
            
            try (PDDocument document = PDDocument.load(file.getInputStream())) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                return pdfStripper.getText(document);
            }
        } else if (fileName.endsWith(".docx")) {
            // Extract text from DOCX
            try (XWPFDocument docx = new XWPFDocument(file.getInputStream())) {
                XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
                return extractor.getText();
            }
        } else if (fileName.endsWith(".doc")) {
           
            try (HWPFDocument doc = new HWPFDocument(file.getInputStream())) {
                WordExtractor extractor = new WordExtractor(doc);
                return extractor.getText();
            }
        } else {
            throw new IOException("Unsupported file format. Please upload a PDF, DOC, or DOCX file.");
        }
    }

}