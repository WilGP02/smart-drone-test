package com.example.droneapp.repository;

import com.example.droneapp.entities.Delivery;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface DeliveryRepository extends ReactiveCrudRepository<Delivery, UUID> {
    Flux<Delivery> findByDroneId(UUID droneId);
}
