package com.ayu.realty.fraud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "측정소 + 최신 소음정보 응답 DTO")
public record DamagedArea(
        String city,
        String gu,
        String damagedHouses

) {}
