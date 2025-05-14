package com.ayu.realty.noise.service;


import com.ayu.realty.noise.entity.NoiseReading;
import com.ayu.realty.noise.entity.NoiseStation;
import com.ayu.realty.noise.repository.NoiseReadingRepository;
import com.ayu.realty.noise.repository.NoiseStationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoiseReadingUploadService {

    private final NoiseStationRepository noiseStationRepository;
    private final NoiseReadingRepository noiseReadingRepository;

    public int uploadNoiseReadings(InputStream noiseExcel, int year) {
        int count = 0;

        try (Workbook workbook = new XSSFWorkbook(noiseExcel)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String city = row.getCell(1).getStringCellValue().trim();       // B열
                String station = row.getCell(2).getStringCellValue().trim();    // C열

                List<NoiseStation> candidates = noiseStationRepository.findByStationNameContaining(station);

                if (candidates.isEmpty()) {
                    log.warn("'{}' 에 해당하는 측정지점 없음", station);
                    continue;
                }

                NoiseStation stationEntity;

                if (candidates.size() == 1) {
                    stationEntity = candidates.get(0);
                    log.debug("'{}' 단일 매칭 성공", station);
                } else {
                    Optional<NoiseStation> match = candidates.stream()
                            .filter(s -> s.getStationName().startsWith(city + " "))
                            .findFirst();

                    if (match.isEmpty()) {
                        log.warn("'{}' 복수 후보 존재, 지역 '{}' 와 일치하는 항목 없음", station, city);
                        continue;
                    }

                    stationEntity = match.get();
                    log.debug("'{}' 복수 매칭 중 지역 '{}' 선택됨", station, city);
                }

                for (int month = 1; month <= 12; month++) {
                    Cell cell = row.getCell(2 + month); // D~O
                    if (cell == null) continue;

                    Double value = null;

                    try {
                        switch (cell.getCellType()) {
                            case NUMERIC -> value = cell.getNumericCellValue();
                            case STRING -> {
                                String raw = cell.getStringCellValue().trim();
                                if (!raw.isBlank() && !raw.equalsIgnoreCase("null")) {
                                    value = Double.parseDouble(raw);
                                }
                            }
                            default -> log.warn("셀 타입 미지원: {} ({} {}월)", cell.getCellType(), station, month);
                        }
                    } catch (Exception e) {
                        log.warn("셀 파싱 실패: {} {}월 - '{}'", station, month, cell.toString(), e);
                        continue;
                    }

                    if (value == null) continue;

                    Optional<NoiseReading> existing = noiseReadingRepository
                            .findByStationAndYearAndMonth(stationEntity, year, month);

                    if (existing.isPresent()) {
                        existing.get().updateValue(value);
                        log.info("갱신: {} {}월 → {} dB", stationEntity.getStationName(), month, value);
                    } else {
                        NoiseReading reading = NoiseReading.builder()
                                .station(stationEntity)
                                .year(year)
                                .month(month)
                                .value(value)
                                .build();

                        noiseReadingRepository.save(reading);
                        log.info("저장: {} {}월 → {} dB", stationEntity.getStationName(), month, value);
                    }

                    count++;
                }
            }

            log.info("소음데이터 저장 완료: {}개", count);
        } catch (Exception e) {
            log.error("소음데이터 엑셀 파싱 오류", e);
        }

        return count;
    }
}
