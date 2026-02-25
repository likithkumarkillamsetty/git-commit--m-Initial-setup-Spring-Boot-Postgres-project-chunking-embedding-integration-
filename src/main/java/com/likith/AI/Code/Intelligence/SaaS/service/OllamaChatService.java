package com.likith.AI.Code.Intelligence.SaaS.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Service
public class OllamaChatService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateAnswer(String prompt) {

        String url = "http://localhost:11434/api/generate";

        Map<String, Object> requestBody = Map.of(
                "model", "gemma:2b",
                "prompt", prompt,
                "stream", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        return (String) response.getBody().get("response");
    }
}