package com.example.droneapp.repository;

import com.example.droneapp.entities.Drone;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DroneRepository extends ReactiveCrudRepository<Drone, UUID> {
}
