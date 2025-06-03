package com.ayu.realty.fraud.service;

import com.ayu.realty.fraud.entity.FraudReading;
import com.ayu.realty.fraud.repository.FraudReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class FraudReadingUploadService {
    private final FraudReadingRepository fraudReadingRepository;

    public int uploadFraudData(InputStream inputStream) {
        int count = 0;

        try (
                InputStreamReader reader = new InputStreamReader(inputStream);
                CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withTrim()
                        .parse(reader)
        ) {
            // BOM 포함 여부와 상관없이 city 헤더 정리
            Map<String, Integer> headerMap = parser.getHeaderMap();
            String cityKey = headerMap.keySet().stream()
                    .filter(k -> k.replace("\uFEFF", "").equalsIgnoreCase("city"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("city 컬럼 없음"));

            for (CSVRecord record : parser) {
                FraudReading reading = FraudReading.builder()
                        .city(record.get(cityKey))
                        .gu(record.get("gu"))
                        .damagedHouses(Integer.parseInt(record.get("damagedHouses")))
                        .build();

                fraudReadingRepository.save(reading);
                count++;
            }

        } catch (Exception e) {
            log.error("피해 CSV 파싱 실패", e);
            throw new IllegalArgumentException("CSV 업로드 실패", e);
        }

        return count;
    }
}