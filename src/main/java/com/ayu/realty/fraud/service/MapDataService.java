package com.ayu.realty.fraud.service;

import com.ayu.realty.global.exception.BaseException;
import com.ayu.realty.global.response.ErrorType.ErrorCode;
import com.ayu.realty.fraud.dto.DamagedArea;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class MapDataService {

    // JSON을 파싱하기 위한 Jackson 라이브러리의 ObjectMapper 인스턴스 생성
    private final ObjectMapper objectMapper = new ObjectMapper();

    // mapdata.json 파일을 읽어 JSON 데이터로 반환하는 메서드
    public List<DamagedArea> getMapData() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("data/mapdata.json")) {
            // 리소스 파일이 없을 경우 예외 처리
            if (is == null) {
                throw new RuntimeException("mapdata.json 파일을 찾을 수 없습니다.");
            }

            return objectMapper.readValue(is,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, DamagedArea.class)
            );

        } catch (Exception e) {
            throw new RuntimeException("mapdata.json 파일 읽기 실패", e);
        }
    }

    public List<DamagedArea> getMapDataByCity(String cityName) {
        List<DamagedArea> allData = getMapData();
        List<DamagedArea> filtered = allData.stream()
                .filter(d -> d.city() != null && cityName.equalsIgnoreCase(d.city()))
                .toList();

        if (filtered.isEmpty()) {
            throw new BaseException(ErrorCode.LOCATION_NOT_FOUND);
        }
        return filtered;
    }

}
