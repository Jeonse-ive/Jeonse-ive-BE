package com.ayu.realty.noise.service;

import com.ayu.realty.noise.entity.NoiseStation;
import com.ayu.realty.noise.repository.NoiseStationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
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

                Cell stationCell = row.getCell(0); // 첫 번째 열이 지점명이라고 가정
                if (stationCell == null) continue;

                String stationName = stationCell.getStringCellValue().trim();
                if (!uniqueStations.add(stationName)) continue; // 중복 skip

                String shortAddress = stationName.replace("지점", "").trim();
                geocodingService.getCoordinates(shortAddress).ifPresent(coord -> {
                    boolean exists = noiseStationRepository.findByStationName(stationName).isPresent();
                    if (!exists) {
                        NoiseStation station = NoiseStation.builder()
                                .stationName(stationName)
                                .shortAddress(shortAddress)
                                .latitude(coord.getLatitude())
                                .longitude(coord.getLongitude())
                                .build();

                        noiseStationRepository.save(station);
                        log.info("저장 완료: {}", stationName);
                    }
                });
            }
        } catch (Exception e) {
            log.error("엑셀 파싱 중 오류 발생", e);
        }
    }
}