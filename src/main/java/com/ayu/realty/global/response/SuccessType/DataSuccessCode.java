package com.ayu.realty.global.response.SuccessType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataSuccessCode implements SuccessType {
    GEO_LOCATION_SUCCESS("G001", "주소 좌표 변환 성공"),
    DATA_SAVED_SUCCESS("D001", "데이터 저장 성공"),
    GET_ALL_LOCATION_SUCCESS("D002", "모든 위치 조회 성공");
    private final String code;
    private final String message;
}
