package com.ayu.realty.noise.service;

import com.ayu.realty.noise.dto.StationInfo;
import com.ayu.realty.noise.entity.NoiseStation;
import com.ayu.realty.noise.repository.NoiseStationRepository;
import com.ayu.realty.noise.util.StationInfoLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StationUploadService {

    private final GeocodingService geocodingService;
    private final NoiseStationRepository noiseStationRepository;
    private final StationInfoLoader stationInfoLoader;

    public int uploadStationInfo(InputStream stationInfoExcel) {
        Map<String, List<StationInfo>> stationMap = stationInfoLoader.load(stationInfoExcel);
        int count = 0;

        try {
            Set<String> inserted = new HashSet<>();

            for (List<StationInfo> group : stationMap.values()) {
                for (StationInfo info : group) {
                    String stationName = info.getCity() + " " + info.getStationName();
                    if (!inserted.add(stationName)) continue;

                    geocodingService.getCoordinates(info.getAddress()).ifPresentOrElse(coord -> {
                        boolean exists = noiseStationRepository.findByStationName(stationName).isPresent();
                        if (!exists) {
                            NoiseStation station = NoiseStation.builder()
                                    .stationName(stationName)
                                    .shortAddress(info.getAddress())
                                    .latitude(coord.getLatitude())
                                    .longitude(coord.getLongitude())
                                    .build();
                            noiseStationRepository.save(station);
                            log.info("저장: {}", stationName);
                        } else {
                            log.info("이미 존재: {}", stationName);
                        }
                    }, () -> log.warn("좌표 변환 실패: {}", info.getAddress()));

                    count++;
                }
            }
        } catch (Exception e) {
            log.error("측정소 엑셀 파싱 오류", e);
        }

        return count;
    }
}
