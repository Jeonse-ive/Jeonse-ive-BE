package com.ayu.realty.noise.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;
import com.ayu.realty.noise.service.NoiseStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Noise Station", description = "환경 소음 측정지점 좌표 변환 및 저장")
@Slf4j
@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class NoiseStationController {

    private final NoiseStationService noiseStationService;

    @Operation(summary = "소음측정지점 데이터 저장", description = "환경소음 데이터 + 측정지점 주소 엑셀 파일을 업로드 받아 DB에 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 저장 완료"),
            @ApiResponse(responseCode = "400", description = "파일 포맷 오류 또는 누락"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiRes<Void>> importStations(
            @Parameter(description = "환경소음(자동) 지점별 월별 데이터 엑셀", required = true)
            @RequestParam("noiseFile") MultipartFile noiseFile,

            @Parameter(description = "측정지점 주소 정보 엑셀", required = true)
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
