package com.ayu.realty.noise.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;
import com.ayu.realty.noise.dto.Coordinate;
import com.ayu.realty.noise.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/geocoding")
@RequiredArgsConstructor
public class GeocodingController {
    private final GeocodingService geocodingService;

    @GetMapping
    public ResponseEntity<ApiRes<Coordinate>> getCoordinates(@RequestParam String address) {
        Optional<Coordinate> coord = geocodingService.getCoordinates(address);
        return coord
                .map(c -> ResponseEntity.ok(ApiRes.success(DataSuccessCode.GEO_LOCATION_SUCCESS, c)))
                .orElse(ResponseEntity.status(ErrorCode.LOCATION_NOT_FOUND.getStatus())
                        .body(ApiRes.fail(ErrorCode.LOCATION_NOT_FOUND)));
    }
}

