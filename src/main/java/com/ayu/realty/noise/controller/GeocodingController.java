package com.ayu.realty.noise.controller;

import com.ayu.realty.noise.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/geocoding")
@RequiredArgsConstructor
public class GeocodingController {
    private final GeocodingService geocodingService;

    @GetMapping
    public ResponseEntity<?> getCoordinates(@RequestParam String address) {
        log.info(">>> [GET] /api/geocoding 요청 수신: address = {}", address);
        return ResponseEntity.ok(geocodingService.getCoordinates(address));
    }
}

