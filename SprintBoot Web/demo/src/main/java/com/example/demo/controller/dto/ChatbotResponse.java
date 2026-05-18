package com.example.demo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponse {
    private String response;
    private String context;
    private Long timestamp;

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
