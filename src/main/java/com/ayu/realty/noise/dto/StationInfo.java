package com.ayu.realty.noise.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "측정지점 주소 정보 DTO")
@Getter
@AllArgsConstructor
public class StationInfo {

    @Schema(description = "도시 이름", example = "서울특별시")
    private String city;

    @Schema(description = "측정지점 이름", example = "회기동")
    private String stationName;

    @Schema(description = "주소", example = "서울특별시 동대문구 회기동")
    private String address;
}