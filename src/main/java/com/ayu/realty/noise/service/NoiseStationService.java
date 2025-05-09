package com.ayu.realty.noise.service;

import com.ayu.realty.noise.entity.NoiseStation;
import com.ayu.realty.noise.repository.NoiseStationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoiseStationService {

    private final GeocodingService geocodingService;
    private final NoiseStationRepository noiseStationRepository;

    public void saveStationsFromExcel(InputStream excelInputStream) {
        try (Workbook workbook = new XSSFWorkbook(excelInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Set<String> uniqueStations = new HashSet<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell cityCell = row.getCell(1);       // B열 (도시)
                Cell rawStationCell = row.getCell(2); // C열 (측정지점명)

                if (rawStationCell == null || cityCell == null) continue;

                String rawStation = rawStationCell.getStringCellValue().trim();
                String city = cityCell.getStringCellValue().trim();

                if (rawStation.startsWith("IOT")) {
                    log.info(">> [제외됨] IOT 지점: {}", rawStation);
                    continue;
                }

                String stationName = city + " " + rawStation;
                if (!uniqueStations.add(stationName)) continue;

                String shortAddress = stationName.replace("지점", "").trim();

                geocodingService.getCoordinates(shortAddress).ifPresentOrElse(coord -> {
                    boolean exists = noiseStationRepository.findByStationName(stationName).isPresent();
                    if (!exists) {
                        NoiseStation station = NoiseStation.builder()
                                .stationName(stationName)
                                .shortAddress(shortAddress)
                                .latitude(coord.getLatitude())
                                .longitude(coord.getLongitude())
                                .build();
                        noiseStationRepository.save(station);
                        log.info("지점 저장 완료: {} ({}, {})", stationName, coord.getLatitude(), coord.getLongitude());
                    } else {
                        log.info("ℹ 이미 저장된 지점: {}", stationName);
                    }
                }, () -> {
                    log.warn("좌표 변환 실패: '{}'", shortAddress);
                });
            }
        } catch (Exception e) {
            log.error("엑셀 파싱 중 오류 발생", e);
        }
    }
}