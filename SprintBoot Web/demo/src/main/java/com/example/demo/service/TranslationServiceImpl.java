package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Service
public class TranslationServiceImpl implements TranslationService {
    private static final Logger logger = LoggerFactory.getLogger(TranslationServiceImpl.class);

    @Value("${google.translate.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String GOOGLE_TRANSLATE_URL = "https://translation.googleapis.com/language/translate/v2";

    @Override
    public String translate(String text, String sourceLanguage, String targetLanguage) {
        try {
            String source = sourceLanguage.equals("es") ? "es" : "en";
            String target = targetLanguage.equals("es") ? "es" : "en";

            String requestBody = String.format(
                "{\"q\":\"%s\",\"source_language\":\"%s\",\"target_language\":\"%s\"}",
                escapeJson(text), source, target
            );

            HttpClient httpClient = HttpClients.createDefault();
            String url = GOOGLE_TRANSLATE_URL + "?key=" + apiKey;
            
            ClassicHttpRequest request = ClassicRequestBuilder.post(url)
                    .setEntity(new StringEntity(requestBody))
                    .setHeader("Content-Type", "application/json")
                    .build();

            return httpClient.execute(request, response -> {
                int status = response.getCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    String content = new String(entity.getContent().readAllBytes());
                    JsonNode jsonNode = objectMapper.readTree(content);
                    
                    // Parse Google Translate API response
                    if (jsonNode.has("data") && jsonNode.get("data").has("translations")) {
                        return jsonNode.get("data").get("translations").get(0).get("translatedText").asText();
                    }
                    return text; // Return original if translation fails
                } else {
                    return "Error en la traducción: " + status;
                }
            });
        } catch (IOException e) {
            logger.error("Error en traducción:", e);
            return "Error al traducir: " + e.getMessage();
        } catch (Exception e) {
            logger.error("Error inesperado en traducción:", e);
            return "Error al traducir: " + e.getMessage();
        }
    }

    private String escapeJson(String text) {
        return text.replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
