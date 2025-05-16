package com.ayu.realty.noise.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "측정소 + 최신 소음정보 응답 DTO")
public record StationWithLatestNoiseRes(
        String stationName,
        String shortAddress,
        double latitude,
        double longitude,
        Double recentValue,
        String recordedAt  // 예: 2024-03
) {}