package org.fpf.governance.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fpf.governance.brain.Brain;
import org.fpf.governance.web.dto.DeconstructResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class FrameActivityController {

    private final Brain brain;
    private final Resource promptResource;
    private final ObjectMapper objectMapper;

    public FrameActivityController(Brain brain, 
                                   @Value("classpath:prompts/deconstruct.txt") Resource promptResource,
                                   ObjectMapper objectMapper) {
        this.brain = brain;
        this.promptResource = promptResource;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/activity/frame")
    public String showFrameActivity() {
        return "activity_frame";
    }

    @PostMapping("/activity/frame/submit")
    public String submitFrame(@RequestParam("description") String description, Model model) {
        try {
            String promptTemplate = StreamUtils.copyToString(promptResource.getInputStream(), StandardCharsets.UTF_8);
            String enhancedPrompt = promptTemplate.replace("{{ user_input }}", description);
            
            String rawResponse = brain.generate(enhancedPrompt);
            String json = extractJson(rawResponse);
            
            DeconstructResponse deconstructResponse = objectMapper.readValue(json, DeconstructResponse.class);
            model.addAttribute("response", deconstructResponse);
            
            return "activity_frame :: response-fragment";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading prompt template: " + e.getMessage());
            return "activity_frame :: error-fragment";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error generating response: " + e.getMessage());
            return "activity_frame :: error-fragment";
        }
    }

    private String extractJson(String text) {
        if (text == null || text.isBlank()) {
            return "{}";
        }

        // Try to find markdown JSON block
        int jsonStart = text.indexOf("```json");
        if (jsonStart != -1) {
            int blockContentStart = jsonStart + 7;
            int jsonEnd = text.indexOf("```", blockContentStart);
            if (jsonEnd != -1) {
                return text.substring(blockContentStart, jsonEnd).trim();
            }
        }

        // Fallback: find first { and last }
        int firstBrace = text.indexOf('{');
        int lastBrace = text.lastIndexOf('}');
        if (firstBrace != -1 && lastBrace != -1 && lastBrace > firstBrace) {
            return text.substring(firstBrace, lastBrace + 1).trim();
        }

        return text.trim();
    }
}
