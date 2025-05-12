package com.ayu.realty.noise.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "Kakao 주소 검색 응답")
@Data
public class KakaoGeoRes {

    @Schema(description = "주소 검색 결과 목록")
    private List<Document> documents;

    @Schema(description = "Kakao 주소 검색 결과 상세")
    @Data
    public static class Document {
        private String x; // longitude
        private String y; // latitude
    }
}

