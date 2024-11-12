package com.example.droneapp.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDto {
    private UUID id;
    private UUID droneId;
    private Double weight;
    private String description;
    private LocalDateTime deliveryTimestamp;
}
