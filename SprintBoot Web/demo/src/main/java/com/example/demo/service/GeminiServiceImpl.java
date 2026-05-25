package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GeminiServiceImpl implements GeminiService {
    private static final Logger logger = LoggerFactory.getLogger(GeminiServiceImpl.class);

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.model:mixtral-8x7b-32768}")
    private String model;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String askChatbot(String message, String context) {
        try {
            // Validar que la API key esté configurada
            if (apiKey == null || apiKey.isEmpty() || apiKey.equals("${groq.api.key}")) {
                logger.error("API key de Groq no está configurada correctamente");
                return "Error: API key de Groq no está configurada. Contacta al administrador.";
            }
            
            String prompt = buildPrompt(message, context);
            
            // Construir request para Groq (compatible con OpenAI)
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("max_tokens", 1024);
            requestBody.put("temperature", 0.7);
            
            // Construir lista de mensajes
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "Eres un asistente inteligente para un hotel boutique llamado Mira Mar. Debes ser amable, profesional y proporcionar información útil. Responde siempre en español.");
            messages.add(systemMessage);
            
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);
            
            requestBody.put("messages", messages);
            
            // Preparar headers con autenticación
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            logger.debug("Enviando solicitud a Groq API con modelo: " + model);
            
            HttpEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            String responseBody = response.getBody();
            JsonNode responseNode = objectMapper.readTree(responseBody);
            
            // Verificar si hay errores en la respuesta de Groq
            if (responseNode.has("error")) {
                String errorMessage = responseNode.get("error").get("message").asText();
                logger.error("Error de Groq API: " + errorMessage);
                return "Error del servicio de IA: " + errorMessage;
            }
            
            // Extraer respuesta del formato OpenAI
            if (responseNode.has("choices") && responseNode.get("choices").isArray()) {
                var choice = responseNode.get("choices").get(0);
                if (choice.has("message") && choice.get("message").has("content")) {
                    return choice.get("message").get("content").asText();
                }
            }
            return "No se pudo generar una respuesta. Por favor intenta de nuevo.";
        } catch (Exception e) {
            logger.error("Error en chatbot: " + e.getClass().getName() + " - " + e.getMessage(), e);
            return "Error al procesar tu pregunta: " + e.getMessage();
        }
    }

    private String buildPrompt(String message, String context) {
        StringBuilder prompt = new StringBuilder();
        
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
