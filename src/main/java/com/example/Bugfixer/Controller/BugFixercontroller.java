package com.example.Bugfixer.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.Bugfixer.Service.GenAIService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class BugFixercontroller {

    @Autowired
    private GenAIService genAIService;

    @GetMapping("/listModels")
    public String listModels() {
        return genAIService.listAvailableModels();
    }

    @PostMapping("/analyze")
    public Map<String, String> analyzeBug(@RequestBody Map<String, String> request) {
        String stacktrace = request.get("stacktrace");
        String codeSnippet = request.get("codeSnippet");

        return genAIService.analyzeBug(stacktrace, codeSnippet);
    }
}
