package com.example.droneapp.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DroneDto {
    private UUID id;
    private String serialNumber;
    private String model;
    private Double maxWeight;
    private Double currentWeight;
    private String status;
}
