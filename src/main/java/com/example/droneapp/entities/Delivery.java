package com.example.droneapp.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("deliveries")
public class Delivery {
    @Id
    private UUID id;
    private UUID droneId;
    private Double weight;
    private String description;
    private LocalDateTime deliveryTimestamp;
}
