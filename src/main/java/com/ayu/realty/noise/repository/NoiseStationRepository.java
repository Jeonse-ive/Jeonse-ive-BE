package com.ayu.realty.noise.repository;

import com.ayu.realty.noise.entity.NoiseStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoiseStationRepository extends JpaRepository<NoiseStation, Long> {

    // 중복 저장 방지를 위한 메서드
    Optional<NoiseStation> findByStationName(String stationName);

    List<NoiseStation> findByStationNameContaining(String station);

    @Query("""
        SELECT DISTINCT s FROM NoiseStation s
        WHERE s.stationName LIKE CONCAT(:city, '%')
        AND EXISTS (
            SELECT 1 FROM NoiseReading r
            WHERE r.station = s
        )
    """)
    List<NoiseStation> findWithReadingsByCity(String city);

}
