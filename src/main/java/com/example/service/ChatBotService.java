package com.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatBotService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String infer(String message) {
        try {
            String url = "https://router.huggingface.co/featherless-ai/v1/completions";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> body = Map.of(
                    "model", "meta-llama/Meta-Llama-3-8B-Instruct",
                    "prompt", message + "\nResponde como asistente, 20 palabras como máximo",
                    "temperature", 0.5,
                    "max_tokens", 60,
                    "top_p", 1,
                    "stop", List.of("Asistente:")
            );

            String jsonBody = objectMapper.writeValueAsString(body);
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            Map<String, Object> jsonMap = objectMapper.readValue(response.getBody(), Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) jsonMap.get("choices");
            return ((String) choices.get(0).get("text")).trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }



}
