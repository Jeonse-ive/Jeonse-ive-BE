package com.ayu.realty.noise.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;
import com.ayu.realty.noise.service.StationUploadService;
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

@Tag(name = "Station Upload", description = "측정소 주소 정보 업로드 API")
@Slf4j
@RestController
@RequestMapping("/api/stations/upload")
@RequiredArgsConstructor
public class StationUploadController {

    private final StationUploadService stationUploadService;

    @Operation(summary = "측정소 정보 업로드", description = "측정지점 주소 정보 엑셀 파일을 업로드하여 DB에 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiRes<Void>> uploadStations(
            @Parameter(description = "측정소 주소 정보 엑셀", required = true)
            @RequestParam("stationInfoFile") MultipartFile stationInfoFile
    ) {
        log.info(" 측정소 정보 업로드: {}", stationInfoFile.getOriginalFilename());

        try {
            int count = stationUploadService.uploadStationInfo(stationInfoFile.getInputStream());
            log.info(" 업로드 완료: {}개 저장됨", count);
            return ResponseEntity.ok(ApiRes.success(DataSuccessCode.GEO_LOCATION_SUCCESS));
        } catch (Exception e) {
            log.error(" 측정소 정보 업로드 실패", e);
            return ResponseEntity
                    .status(ErrorCode.MEASUREMENT_IMPORT_FAILED.getStatus())
                    .body(ApiRes.fail(ErrorCode.MEASUREMENT_IMPORT_FAILED));
        }
    }
}
