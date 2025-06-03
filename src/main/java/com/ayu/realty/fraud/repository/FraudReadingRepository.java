package com.ayu.realty.fraud.repository;

import com.ayu.realty.fraud.entity.FraudReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FraudReadingRepository extends JpaRepository<FraudReading, Long>{
    List<FraudReading> findByCity(String city);
}