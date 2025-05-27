package com.ayu.realty.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class testController {

    @GetMapping("/api/test")
    public Map<String, String> testApiConnection() {
        return Map.of("message", "API 연결 성공");
    }
}
