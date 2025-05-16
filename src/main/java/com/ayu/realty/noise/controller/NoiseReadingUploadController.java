package com.ayu.realty.noise.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;
import com.ayu.realty.noise.service.NoiseReadingUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Noise Reading Upload", description = "소음데이터 업로드 API")
@Slf4j
@RestController
@RequestMapping("/api/readings/upload")
@RequiredArgsConstructor
public class NoiseReadingUploadController {


    private final NoiseReadingUploadService noiseReadingUploadService;

    @Operation(summary = "소음데이터 업로드", description = "소음 측정 데이터 엑셀 파일을 업로드하여 NoiseReading 데이터로 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiRes<Integer>> uploadReadings(
            @Parameter(description = "소음 측정 데이터 엑셀", required = true)
            @RequestParam("noiseFile") MultipartFile noiseFile,

            @Parameter(description = "데이터 연도 (예: 2024)", required = true, example = "2024")
            @RequestParam("year") int year
    ) {
        log.info("소음데이터 업로드: {}, year={}", noiseFile.getOriginalFilename(), year);

        try {
            int count = noiseReadingUploadService.uploadNoiseReadings(noiseFile.getInputStream(), year);
            log.info("업로드 완료: {}건 저장됨", count);
            return ResponseEntity.ok(ApiRes.success(DataSuccessCode.DATA_SAVED_SUCCESS, count));
        } catch (Exception e) {
            log.error("소음데이터 업로드 실패", e);
            return ResponseEntity
                    .status(ErrorCode.MEASUREMENT_IMPORT_FAILED.getStatus())
                    .body(ApiRes.fail(ErrorCode.MEASUREMENT_IMPORT_FAILED));
        }
    }
}
