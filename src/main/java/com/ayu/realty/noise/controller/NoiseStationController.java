package com.ayu.realty.noise.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;
import com.ayu.realty.noise.service.NoiseStationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class NoiseStationController {

    private final NoiseStationService noiseStationService;

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiRes<Void>> importStations(
            @RequestParam("noiseFile") MultipartFile noiseFile,
            @RequestParam("stationInfoFile") MultipartFile stationInfoFile
    ) {
        log.info(">> 요청 수신: noiseFile = {}, stationInfoFile = {}", noiseFile.getOriginalFilename(), stationInfoFile.getOriginalFilename());

        try {
            noiseStationService.importStations(noiseFile.getInputStream(), stationInfoFile.getInputStream());
            return ResponseEntity.ok(ApiRes.success(DataSuccessCode.GEO_LOCATION_SUCCESS));
        } catch (Exception e) {
            log.error("엑셀 업로드 실패", e);
            return ResponseEntity
                    .status(ErrorCode.MEASUREMENT_IMPORT_FAILED.getStatus())
                    .body(ApiRes.fail(ErrorCode.MEASUREMENT_IMPORT_FAILED));
        }
    }
}
