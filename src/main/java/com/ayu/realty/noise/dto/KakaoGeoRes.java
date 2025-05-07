package com.ayu.realty.noise.dto;

import lombok.Data;

import java.util.List;

@Data
public class KakaoGeoRes {
    private List<Document> documents;

    @Data
    public static class Document {
        private String x; // longitude
        private String y; // latitude
    }
}

