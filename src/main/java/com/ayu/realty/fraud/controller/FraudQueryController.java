package com.ayu.realty.fraud.controller;
import com.ayu.realty.fraud.entity.FraudReading;
import com.ayu.realty.fraud.service.FraudReadingQueryService;
import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "Fraud Query", description = "피해 주택 수 조회 API")
@RestController
@RequestMapping("/api/fraud")
@RequiredArgsConstructor
public class FraudQueryController {
    private final FraudReadingQueryService fraudReadingQueryService;

    @Operation(summary = "도시별 피해 주택 수 조회", description = "city 값에 해당하는 모든 구의 피해 주택 수를 반환합니다.")
    @GetMapping("/count")
    public ResponseEntity<ApiRes<List<FraudReading>>> getFraudCountsByCity(
            @Parameter(description = "도시 이름 (예: Seoul)", required = true)
            @RequestParam String city
    ) {
        List<FraudReading> result = fraudReadingQueryService.findByCity(city);
        return ResponseEntity.ok(ApiRes.success(DataSuccessCode.GET_ALL_LOCATION_SUCCESS, result));
    }
}
