package com.ayu.realty.noise.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.noise.dto.response.StationWithLatestNoiseRes;
import com.ayu.realty.noise.service.StationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;

import java.util.List;

@Tag(name = "Noise Query", description = "측정소 및 최신 소음정보 조회 API")
@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class NoiseQueryController {

    private final StationQueryService stationQueryService;

    @Operation(summary = "모든 측정소 조회", description = "등록된 측정소와 각 측정소의 최신 소음 데이터를 반환합니다.")
    @GetMapping
    public ResponseEntity<ApiRes<List<StationWithLatestNoiseRes>>> getAllStationsWithLatestNoise() {
        List<StationWithLatestNoiseRes> result = stationQueryService.findAllWithLatestReading();
        return ResponseEntity.ok(ApiRes.success(DataSuccessCode.GET_ALL_LOCATION_SUCCESS, result));
    }
}
