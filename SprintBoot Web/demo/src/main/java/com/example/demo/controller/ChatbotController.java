package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.demo.controller.dto.ChatbotRequest;
import com.example.demo.controller.dto.ChatbotResponse;
import com.example.demo.service.GeminiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Chatbot", description = "Chatbot inteligente del hotel")
@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'CLIENT')")
public class ChatbotController {

    @Autowired
    private GeminiService geminiService;

    @PostMapping("/ask")
    @Operation(summary = "Hacer una pregunta al chatbot")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'CLIENT')")
    public ResponseEntity<?> ask(@RequestBody ChatbotRequest request) {
        try {
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El mensaje no puede estar vacío");
            }

            String response = geminiService.askChatbot(request.getMessage(), request.getContext());
            
            ChatbotResponse chatResponse = new ChatbotResponse();
            chatResponse.setResponse(response);
            chatResponse.setContext(request.getContext());
            chatResponse.setTimestamp(System.currentTimeMillis());

            return ResponseEntity.ok(chatResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la pregunta: " + e.getMessage());
        }
    }
}
