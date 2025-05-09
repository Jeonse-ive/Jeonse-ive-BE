package com.ayu.realty.noise.service;

import com.ayu.realty.noise.dto.Coordinate;
import com.ayu.realty.noise.dto.KakaoGeoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeocodingService {

    private final RestTemplate restTemplate;
    private final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    @Value("${kakao.rest-api-key}")
    private String KAKAO_API_KEY;

    public Optional<Coordinate> getCoordinates(String address) {
        log.info(">> [주소 변환 요청] 입력 주소: {}", address);
        log.debug(">> [Kakao API Key] {}", KAKAO_API_KEY);

        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_API_URL)
                    .queryParam("query", address)
                    .build()
                    .encode()
                    .toUri();

            log.debug(">> 요청 URI: {}", uri);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<KakaoGeoRes> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity, KakaoGeoRes.class
            );

            log.debug(">> 응답 상태 코드: {}", response.getStatusCode());

            KakaoGeoRes body = response.getBody();
            if (body != null && !body.getDocuments().isEmpty()) {
                KakaoGeoRes.Document doc = body.getDocuments().get(1);
                Double lat = Double.parseDouble(doc.getY());
                Double lng = Double.parseDouble(doc.getX());

                log.info(">> [좌표 변환 성공] 위도: {}, 경도: {}", lat, lng);
                return Optional.of(new Coordinate(lat, lng));
            } else {
                log.warn(">> [좌표 변환 실패] 결과 없음 - 입력 주소: {}", address);
            }

        } catch (Exception e) {
            log.error(">> [예외 발생] 주소 변환 중 오류 - 입력 주소: {}", address, e);
        }

        return Optional.empty();
    }
}