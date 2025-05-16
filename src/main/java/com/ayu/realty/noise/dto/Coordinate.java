package com.ayu.realty.noise.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "좌표 정보 DTO")
@Getter
@AllArgsConstructor
public class Coordinate {
    @Schema(description = "위도 (Latitude)", example = "37.5665")
    private Double latitude;

    @Schema(description = "경도 (Longitude)", example = "126.9780")
    private Double longitude;
}

