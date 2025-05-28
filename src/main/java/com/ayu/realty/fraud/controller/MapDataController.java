package com.ayu.realty.fraud.controller;

import com.ayu.realty.global.dto.ApiRes;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;
import com.ayu.realty.fraud.dto.DamagedArea;
import com.ayu.realty.fraud.service.MapDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Damaged house Query", description = "전세사기 피해주택 수 조회 API")
@RestController  // REST API용 컨트롤러임을 명시
@RequestMapping("/api/mapdata")  // 기본 URI 설정
@RequiredArgsConstructor  // final 필드에 대해 생성자 자동 생성 (DI에 사용)
public class MapDataController {

    private final MapDataService mapDataService;  // MapDataService 주입

   @Operation(summary = "전세사기 위험 지도", description = "도시의 이름을 입력 받아 문제 주택 수를 반환합니다.")
   @GetMapping
    public ResponseEntity<ApiRes<List<DamagedArea>>>getDamagedLocalData(@RequestParam(defaultValue = "all") String city){
       if ("all".equalsIgnoreCase(city)) {
           List<DamagedArea> result = mapDataService.getMapData();
           return ResponseEntity.ok(ApiRes.success(DataSuccessCode.GET_ALL_LOCATION_SUCCESS, result));
       } else {
           List<DamagedArea> result = mapDataService.getMapDataByCity(city);
           return ResponseEntity.ok(ApiRes.success(DataSuccessCode.GET_ALL_LOCATION_SUCCESS, result));
       }


   }

}
