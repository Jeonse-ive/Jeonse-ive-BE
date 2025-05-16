package com.ayu.realty.noise.util;
import com.ayu.realty.noise.dto.StationInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Slf4j
@Component
public class StationInfoLoader {

    public Map<String, List<StationInfo>> load(InputStream inputStream) {
        Map<String, List<StationInfo>> map = new HashMap<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell cityCell = row.getCell(1);   // B열: 도시
                Cell nameCell = row.getCell(2);   // C열: 측정지점
                Cell addrCell = row.getCell(3);   // D열: 주소

                if (cityCell == null || nameCell == null || addrCell == null) continue;

                String city = cityCell.getStringCellValue().trim();
                String name = nameCell.getStringCellValue().trim();
                String address = addrCell.getStringCellValue().trim();

                StationInfo info = new StationInfo(city, name, address);
                map.computeIfAbsent(name, k -> new ArrayList<>()).add(info);
            }
        } catch (Exception e) {
            log.error("측정지점 정보 엑셀 파싱 실패", e);
        }

        return map;
    }
}