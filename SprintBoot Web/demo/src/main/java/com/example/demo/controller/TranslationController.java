package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.TranslationRequest;
import com.example.demo.controller.dto.TranslationResponse;
import com.example.demo.service.TranslationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/translation")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Traducción", description = "Servicio de traducción automática")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @PostMapping("/translate")
    @Operation(summary = "Traducir texto de un idioma a otro")
    public ResponseEntity<?> translate(@RequestBody TranslationRequest request) {
        try {
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El texto no puede estar vacío");
            }

            String translated = translationService.translate(
                request.getText(),
                request.getSourceLanguage(),
                request.getTargetLanguage()
            );

            TranslationResponse response = new TranslationResponse();
            response.setOriginalText(request.getText());
            response.setTranslatedText(translated);
            response.setSourceLanguage(request.getSourceLanguage());
            response.setTargetLanguage(request.getTargetLanguage());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al traducir: " + e.getMessage());
        }
    }
}
