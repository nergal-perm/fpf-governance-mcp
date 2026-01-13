package org.fpf.governance.web;

import org.fpf.governance.brain.Brain;
import org.neo4j.driver.Driver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatusController {

    private final Driver neo4jDriver;
    private final Brain brain;

    public StatusController(Driver neo4jDriver, Brain brain) {
        this.neo4jDriver = neo4jDriver;
        this.brain = brain;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/status/db")
    @ResponseBody
    public String checkDbStatus() {
        try {
            neo4jDriver.verifyConnectivity();
            return "<span class=\"text-green-600 font-bold\">✅ Connected</span>";
        } catch (Exception e) {
            return "<span class=\"text-red-600 font-bold\">❌ Failed: " + e.getMessage() + "</span>";
        }
    }

    @PostMapping("/test-brain")
    @ResponseBody
    public String testBrain(@RequestParam("prompt") String prompt) {
        try {
            String response = brain.generate(prompt);
            // Simple HTML escaping could be added here if needed, but for internal test tool strict escaping is secondary.
            // Using a simple pre tag for display.
            return "<div class=\"p-4 bg-gray-100 rounded border mt-2\"><h4 class=\"font-bold\">Brain Response:</h4><pre class=\"whitespace-pre-wrap\">" + response + "</pre></div>";
        } catch (Exception e) {
            return "<div class=\"p-4 bg-red-100 text-red-700 rounded border mt-2\"><strong>Error:</strong> " + e.getMessage() + "</div>";
        }
    }
}
