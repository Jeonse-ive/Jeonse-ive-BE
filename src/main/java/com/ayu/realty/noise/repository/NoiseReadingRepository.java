package com.ayu.realty.noise.repository;

import com.ayu.realty.noise.entity.NoiseReading;
import com.ayu.realty.noise.entity.NoiseStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoiseReadingRepository extends JpaRepository<NoiseReading, Long> {

    Optional<NoiseReading> findTopByStationOrderByYearDescMonthDesc(NoiseStation station);

    Optional<NoiseReading> findByStationAndYearAndMonth(NoiseStation station, int year, int month);




}