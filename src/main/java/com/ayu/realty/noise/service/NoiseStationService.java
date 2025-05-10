package com.ayu.realty.noise.service;

import com.ayu.realty.noise.dto.StationInfo;
import com.ayu.realty.noise.entity.NoiseStation;
import com.ayu.realty.noise.repository.NoiseStationRepository;
import com.ayu.realty.noise.util.StationInfoLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoiseStationService {

    private final GeocodingService geocodingService;
    private final NoiseStationRepository noiseStationRepository;
    private final StationInfoLoader stationInfoLoader;

    public void importStations(InputStream noiseExcel, InputStream stationInfoExcel) {
        Map<String, List<StationInfo>> stationMap = stationInfoLoader.load(stationInfoExcel);

        try (Workbook workbook = new XSSFWorkbook(noiseExcel)) {
            Sheet sheet = workbook.getSheetAt(0);
            Set<String> inserted = new HashSet<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String city = row.getCell(1).getStringCellValue().trim();    // B열
                String station = row.getCell(2).getStringCellValue().trim(); // C열
                String stationName = city + " " + station;

                if (!inserted.add(stationName)) continue; // 중복 방지

                List<StationInfo> candidates = stationMap.getOrDefault(station, List.of());

                if (candidates.isEmpty()) {
                    log.warn("해당 측정지점명 '{}' 이 station_info.xlsx 에 존재하지 않음", station);
                    continue;
                }

                StationInfo matched;

                if (candidates.size() == 1) {
                    matched = candidates.get(0);
                    log.debug("측정지점명 '{}' 매칭 성공 (단일 후보)", station);
                } else {
                    Optional<StationInfo> match = candidates.stream()
                            .filter(info -> info.getCity().equals(city))
                            .findFirst();

                    if (match.isEmpty()) {
                        log.warn("측정지점명 '{}' 은 있지만 도시 '{}' 와 일치하는 항목 없음 (후보 {}개: {})",
                                station, city, candidates.size(),
                                candidates.stream().map(StationInfo::getCity).toList());
                        continue;
                    }

                    matched = match.get();
                    log.debug("측정지점명 '{}' 매칭 성공 (복수 후보 중 도시 '{}' 일치)", station, city);
                }

                String address = matched.getAddress();

                geocodingService.getCoordinates(address).ifPresentOrElse(coord -> {
                    boolean exists = noiseStationRepository.findByStationName(stationName).isPresent();
                    if (!exists) {
                        NoiseStation saved = NoiseStation.builder()
                                .stationName(stationName)
                                .shortAddress(address)
                                .latitude(coord.getLatitude())
                                .longitude(coord.getLongitude())
                                .build();
                        noiseStationRepository.save(saved);
                        log.info("저장 완료: {} → {}, {}", stationName, coord.getLatitude(), coord.getLongitude());
                    } else {
                        log.info("이미 저장된 지점: {}", stationName);
                    }
                }, () -> log.warn("좌표 변환 실패: {}", address));
            }

        } catch (Exception e) {
            log.error("Noise 엑셀 파싱 중 오류", e);
        }
    }

}