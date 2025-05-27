package com.ayu.realty.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @GetMapping("/api/test")
    public String testApiConnection() {
        return "API 연결 성공";
    }
}
