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
            requestBody.put("max_tokens", 320);
            requestBody.put("temperature", 0.4);
            
            // Construir lista de mensajes
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "Eres el concierge digital de un hotel boutique llamado Mira Mar. Responde en español, breve y directo. No uses Markdown ni asteriscos. Evita introducciones largas y da respuestas claras.");
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
        String normalizedContext = context == null ? "" : context.trim();
        String normalizedMessage = message == null ? "" : message.toLowerCase();
        boolean looksLikeRooms = normalizedContext.isEmpty()
            && (normalizedMessage.contains("habitacion") || normalizedMessage.contains("suite"));

        prompt.append("Responde breve y directo. ");
        prompt.append("Sin Markdown, sin asteriscos y sin emojis. ");
        prompt.append("Usa texto plano. ");
        prompt.append("Si incluyes lista, usa guion '-' al inicio de cada linea. ");
        prompt.append("Maximo 4 o 5 items. ");
        prompt.append("Evita saludos largos. ");
        
        if ("rooms".equals(normalizedContext) || looksLikeRooms) {
            prompt.append("El usuario pregunta sobre habitaciones. Responde con lista corta de opciones. ");
            prompt.append("Formato por item: Nombre - detalle corto - precio por noche. ");
        } else if ("prices".equals(normalizedContext)) {
            prompt.append("El usuario pregunta sobre precios. Da un rango o ejemplos claros y pregunta por fechas si faltan. ");
        } else if ("services".equals(normalizedContext)) {
            prompt.append("El usuario pregunta sobre servicios del hotel. Da una lista corta de servicios clave. ");
        } else if ("reservations".equals(normalizedContext)) {
            prompt.append("El usuario quiere hacer una reserva. Explica el proceso en 2 o 3 pasos maximo. ");
        }
        
        prompt.append("\n\nPregunta del cliente: ").append(message);
        return prompt.toString();
    }
}
