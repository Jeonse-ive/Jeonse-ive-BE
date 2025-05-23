package com.ayu.realty.noise.controller;

import com.ayu.realty.noise.entity.NoiseStation;
import com.ayu.realty.noise.repository.NoiseStationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class NoiseTestController {

    private final NoiseStationRepository noiseStationRepository;

    @PostMapping("/insert-korean")
    public String insertTestStation() {
        String stationName = "서울 강남구 테스트지점";
        String address = "서울특별시 강남구 역삼동 123-45";

        log.info("🚀 테스트 한글 로그 출력: stationName={}, address={}", stationName, address);

        NoiseStation station = NoiseStation.builder()
                .stationName(stationName)
                .shortAddress(address)
                .latitude(37.123456)
                .longitude(127.123456)
                .build();

        noiseStationRepository.save(station);

        return "한글 테스트 데이터 저장 완료";
    }
}
