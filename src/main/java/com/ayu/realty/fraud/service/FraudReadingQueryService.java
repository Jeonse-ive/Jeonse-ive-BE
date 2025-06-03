package com.ayu.realty.fraud.service;

import com.ayu.realty.fraud.repository.FraudReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ayu.realty.fraud.entity.FraudReading;


import java.util.List;

@Service
@RequiredArgsConstructor
public class FraudReadingQueryService {

    private final FraudReadingRepository fraudReadingRepository;

    public List<FraudReading> findByCity(String city) {
        return fraudReadingRepository.findByCity(city);
    }
}
