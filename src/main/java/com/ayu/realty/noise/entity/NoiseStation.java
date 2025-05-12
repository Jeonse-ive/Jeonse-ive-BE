package com.ayu.realty.noise.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "noise_station")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NoiseStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String stationName; // 예: 서울특별시지점

    @Column(nullable = false)
    private String shortAddress;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
}
