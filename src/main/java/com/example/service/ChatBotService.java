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

    @Value("${open.router.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String infer(String prompt){
        String url = "https://openrouter.ai/api/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        Map<String, Object> body = Map.of(
                "model", "mistralai/mistral-7b-instruct",
                "messages", new Object[]{
                        Map.of("role","user","content",
                                "Responde la pregunta en un máximo de 200 carácteres. " +prompt)
                },
                "max_tokens", 200

        );
        HttpEntity<Map<String,Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
        }
    }


