package com.ayu.realty.noise.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;
import com.ayu.realty.noise.dto.Coordinate;
import com.ayu.realty.noise.service.GeocodingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Geocoding", description = "주소 → 좌표 변환 API")
@Slf4j
@RestController
@RequestMapping("/api/geocoding")
@RequiredArgsConstructor
public class GeocodingController {
    private final GeocodingService geocodingService;

    @Operation(summary = "주소 좌표 변환", description = "입력한 주소 문자열을 통해 Kakao API를 사용하여 위도/경도를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좌표 조회 성공"),
            @ApiResponse(responseCode = "404", description = "주소 매칭 실패")
    })
    @GetMapping
    public ResponseEntity<ApiRes<Coordinate>> getCoordinates(
            @Parameter(description = "도로명 또는 지번 주소", required = true)
            @RequestParam String address) {
        Optional<Coordinate> coord = geocodingService.getCoordinates(address);
        return coord
                .map(c -> ResponseEntity.ok(ApiRes.success(DataSuccessCode.GEO_LOCATION_SUCCESS, c)))
                .orElse(ResponseEntity.status(ErrorCode.LOCATION_NOT_FOUND.getStatus())
                        .body(ApiRes.fail(ErrorCode.LOCATION_NOT_FOUND)));
    }
}

