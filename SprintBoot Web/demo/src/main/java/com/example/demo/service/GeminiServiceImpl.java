package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GeminiServiceImpl implements GeminiService {
    private static final Logger logger = LoggerFactory.getLogger(GeminiServiceImpl.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model:gemini-1.5-flash}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String askChatbot(String message, String context) {
        try {
            String prompt = buildPrompt(message, context);
            String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;
            
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> partMap = new HashMap<>();
            partMap.put("text", prompt);
            Map<String, Object> contentMap = new HashMap<>();
            contentMap.put("parts", new Object[]{partMap});
            requestBody.put("contents", new Object[]{contentMap});
            
            String response = restTemplate.postForObject(url, requestBody, String.class);
            JsonNode responseNode = objectMapper.readTree(response);
            
            if (responseNode.has("candidates") && responseNode.get("candidates").isArray()) {
                var candidate = responseNode.get("candidates").get(0);
                if (candidate.has("content") && candidate.get("content").has("parts")) {
                    return candidate.get("content").get("parts").get(0).get("text").asText();
                }
            }
            return "No se pudo generar una respuesta.";
        } catch (Exception e) {
            logger.error("Error en chatbot:", e);
            return "Error al procesar tu pregunta. Intenta de nuevo.";
        }
    }

    private String buildPrompt(String message, String context) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Eres un asistente inteligente para un hotel boutique llamado Mira Mar. ");
        prompt.append("Debes ser amable, profesional y proporcionar información útil. ");
        prompt.append("Responde siempre en español. ");
        
        if ("rooms".equals(context)) {
            prompt.append("El usuario pregunta sobre habitaciones. Proporciona información sobre tipos de habitaciones disponibles. ");
        } else if ("prices".equals(context)) {
            prompt.append("El usuario pregunta sobre precios. Proporciona información sobre tarifas y disponibilidad. ");
        } else if ("services".equals(context)) {
            prompt.append("El usuario pregunta sobre servicios del hotel. Menciona servicios como: Wi-Fi gratuito, piscina, spa, restaurante, gym. ");
        } else if ("reservations".equals(context)) {
            prompt.append("El usuario quiere hacer una reserva. Explica el proceso de reservación. ");
        }
        
        prompt.append("\n\nPregunta del cliente: ").append(message);
        return prompt.toString();
    }
}
