package com.ayu.realty.fraud.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fraud_reading")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FraudReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String gu;

    @Column(name = "damaged_houses")
    private int damagedHouses;
}
