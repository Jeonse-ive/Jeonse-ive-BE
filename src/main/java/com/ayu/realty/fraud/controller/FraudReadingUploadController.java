package com.ayu.realty.fraud.controller;

import com.ayu.realty.fraud.service.FraudReadingUploadService;
import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;
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

@Tag(name = "Fraud Reading Upload", description = "피해 주택 수 데이터 업로드 API")
@Slf4j
@RestController
@RequestMapping("/api/fraud")
@RequiredArgsConstructor
public class FraudReadingUploadController {

    private final FraudReadingUploadService fraudReadingUploadService;

    @Operation(summary = "피해 주택 수 업로드", description = "CSV 파일을 업로드하여 피해 주택 수 데이터를 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiRes<Integer>> uploadFraudData(
            @Parameter(description = "피해 주택 수 CSV 파일", required = true)
            @RequestParam("fraudFile") MultipartFile fraudFile
    ) {
        log.info("피해 주택 수 데이터 업로드 시작: {}", fraudFile.getOriginalFilename());

        try {
            int savedCount = fraudReadingUploadService.uploadFraudData(fraudFile.getInputStream());
            log.info("업로드 완료: {}건 저장됨", savedCount);
            return ResponseEntity.ok(ApiRes.success(DataSuccessCode.DATA_SAVED_SUCCESS, savedCount));
        } catch (Exception e) {
            log.error("피해 데이터 업로드 실패", e);
            return ResponseEntity
                    .status(ErrorCode.MEASUREMENT_IMPORT_FAILED.getStatus())
                    .body(ApiRes.fail(ErrorCode.MEASUREMENT_IMPORT_FAILED));
        }
    }
}