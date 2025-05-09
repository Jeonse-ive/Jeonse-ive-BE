package com.ayu.realty.noise.repository;

import com.ayu.realty.noise.entity.NoiseStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoiseStationRepository extends JpaRepository<NoiseStation, Long> {

    // 중복 저장 방지를 위한 메서드
    Optional<NoiseStation> findByStationName(String stationName);

    // 필요한 경우 shortAddress로도 조회 가능
    Optional<NoiseStation> findByShortAddress(String shortAddress);
}
