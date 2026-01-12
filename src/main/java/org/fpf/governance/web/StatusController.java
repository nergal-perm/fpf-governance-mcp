package org.fpf.governance.web;

import org.neo4j.driver.Driver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatusController {

    private final Driver neo4jDriver;

    public StatusController(Driver neo4jDriver) {
        this.neo4jDriver = neo4jDriver;
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
}
