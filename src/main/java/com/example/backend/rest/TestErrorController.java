package com.example.backend.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestErrorController {
    @GetMapping("/actuator/test-error")
    public String triggerError() {
        throw new RuntimeException("Simulated server error for alert test");
    }

}
