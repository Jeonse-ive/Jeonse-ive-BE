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
        log.info(">> Kakao 주소 변환 요청: {}", address);
        log.info(">> Kakao API Key = {}", KAKAO_API_KEY); // 이거 추가
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_API_URL)
                    .queryParam("query", address)
                    .build()
                    .encode()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<KakaoGeoRes> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity, KakaoGeoRes.class
            );

            if (response.getBody() != null && !response.getBody().getDocuments().isEmpty()) {
                KakaoGeoRes.Document doc = response.getBody().getDocuments().get(0);
                return Optional.of(new Coordinate(doc.getY(), doc.getX()));
            }
        } catch (Exception e) {
            log.error("Kakao 주소 변환 실패: {}", address, e);
        }
        return Optional.empty();
    }
}