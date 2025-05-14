package com.ayu.realty.noise.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "noise_reading",
        uniqueConstraints = @UniqueConstraint(columnNames = {"station_id", "year", "month"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NoiseReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private NoiseStation station;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private double value;

    public void updateValue(double newValue) {
        this.value = newValue;
    }
}

