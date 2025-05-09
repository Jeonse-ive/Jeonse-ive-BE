package com.ayu.realty.global.response.SuccessType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataSuccessCode implements SuccessType {
    GEO_LOCATION_SUCCESS("G001", "주소 좌표 변환 성공");

    private final String code;
    private final String message;
}
