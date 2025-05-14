package com.ayu.realty.noise.service;

import com.ayu.realty.noise.dto.response.StationWithLatestNoiseRes;
import com.ayu.realty.noise.entity.NoiseReading;
import com.ayu.realty.noise.entity.NoiseStation;
import com.ayu.realty.noise.repository.NoiseReadingRepository;
import com.ayu.realty.noise.repository.NoiseStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StationQueryService
{

    private final NoiseStationRepository noiseStationRepository;
    private final NoiseReadingRepository noiseReadingRepository;

    public List<StationWithLatestNoiseRes> findAllWithLatestReading() {
        List<NoiseStation> stations = noiseStationRepository.findAll();

        return stations.stream()
                .map(station -> {
                    Optional<NoiseReading> latest = noiseReadingRepository
                            .findTopByStationOrderByYearDescMonthDesc(station);

                    return StationWithLatestNoiseRes.builder()
                            .stationName(station.getStationName())
                            .shortAddress(station.getShortAddress())
                            .latitude(station.getLatitude())
                            .longitude(station.getLongitude())
                            .recentValue(latest.map(NoiseReading::getValue).orElse(null))
                            .recordedAt(latest.map(r -> String.format("%d-%02d", r.getYear(), r.getMonth())).orElse(null))
                            .build();
                })
                .toList();
    }

}
