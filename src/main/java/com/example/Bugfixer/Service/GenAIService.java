package com.example.Bugfixer.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class GenAIService {

    @Value("${genai.apikey}")
    private String apiKey;

    // Method to list available models (Google Gemini 1.5)
    public String listAvailableModels() {
        String url = "https://generativelanguage.googleapis.com/v1beta/models?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // Return the list of models or error message
        return response.getBody();
    }

    // Method to analyze bugs using Gemini 1.5 model
    public Map<String, String> analyzeBug(String stacktrace, String codeSnippet) {
        String prompt = String.format("""
            Analyze this Java stack trace and code snippet:
            Stacktrace:
            %s

            Code Snippet:
            %s

            Respond in JSON format like:
            {
              "rootCause": "...",
              "fixSuggestion": "..."
            }
            """, stacktrace, codeSnippet);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Corrected request body without 'input' and 'parameters'
        String requestBody = String.format("""
            {
                "prompt": "%s",
                "max_output_tokens": 500,
                "temperature": 0.7
            }
            """, prompt.replace("\"", "\\\""));  // Escape double quotes in the prompt

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        try {
            String responseText = JsonParser.parseString(response.getBody())
                    .getAsJsonObject()
                    .getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();

            JsonObject resultJson = JsonParser.parseString(responseText).getAsJsonObject();

            Map<String, String> result = new HashMap<>();
            result.put("rootCause", resultJson.get("rootCause").getAsString());
            result.put("fixSuggestion", resultJson.get("fixSuggestion").getAsString());
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error parsing response from Gemini API: " + e.getMessage(), e);
        }
    }
}    
