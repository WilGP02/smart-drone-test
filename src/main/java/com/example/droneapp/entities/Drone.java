package com.example.droneapp.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("drones")
public class Drone {
    @Id
    private UUID id;
    private String serialNumber;
    private String model;
    private Double maxWeight;
    private Double currentWeight;
    private String status;
}
