package com.example.droneapp.service;

import com.example.droneapp.dto.DeliveryDto;
import com.example.droneapp.dto.DroneDto;
import com.example.droneapp.mapper.DeliveryMapper;
import com.example.droneapp.mapper.DroneMapper;
import com.example.droneapp.repository.DeliveryRepository;
import com.example.droneapp.repository.DroneRepository;
import com.example.droneapp.entities.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneRepository droneRepository;
    private final DeliveryRepository deliveryRepository;

    public Mono<DroneDto> createDrone(Mono<DroneDto> droneDtoMono) {
        return droneDtoMono.map(DroneMapper.INSTANCE::toEntity)
                .flatMap(droneRepository::save)
                .map(DroneMapper.INSTANCE::toDto);
    }

    public Flux<DroneDto> getAllDrones() {
        return droneRepository.findAll().map(DroneMapper.INSTANCE::toDto);
    }

    public Mono<DroneDto> getDroneById(UUID id) {
        return droneRepository.findById(id).map(DroneMapper.INSTANCE::toDto);
    }

    public Mono<DroneDto> loadDrone(UUID id, double weight) {
        return droneRepository.findById(id)
                .flatMap(drone -> {
                    if (!"AVAILABLE".equals(drone.getStatus())) {
                        return Mono.error(new IllegalStateException("Drone is not available for loading"));
                    }
                    if (drone.getCurrentWeight() + weight > drone.getMaxWeight()) {
                        return Mono.error(new IllegalArgumentException("Exceeds max weight"));
                    }
                    drone.setCurrentWeight(drone.getCurrentWeight() + weight);
                    drone.setStatus("LOADING");
                    return droneRepository.save(drone);
                })
                .map(DroneMapper.INSTANCE::toDto);
    }

    public Mono<DroneDto> deliverPackage(UUID id, DeliveryDto deliveryDto) {
        Delivery delivery = DeliveryMapper.INSTANCE.toEntity(deliveryDto);
        return droneRepository.findById(id)
                .flatMap(drone -> {
                    if (!"LOADING".equals(drone.getStatus())) {
                        return Mono.error(new IllegalStateException("Drone is not ready for delivery"));
                    }
                    if (delivery.getWeight() > drone.getCurrentWeight()) {
                        return Mono.error(new IllegalArgumentException("Delivery weight exceeds current load"));
                    }
                    drone.setCurrentWeight(drone.getCurrentWeight() - delivery.getWeight());
                    drone.setStatus(drone.getCurrentWeight() == 0 ? "AVAILABLE" : "LOADING");
                    return deliveryRepository.save(delivery).then(droneRepository.save(drone));
                })
                .map(DroneMapper.INSTANCE::toDto);
    }

    public Flux<DeliveryDto> getDroneDeliveries(UUID id) {
        return deliveryRepository.findByDroneId(id).map(DeliveryMapper.INSTANCE::toDto);
    }

    public Mono<Void> deleteDrone(UUID id) {
        return deliveryRepository.findByDroneId(id)
                .hasElements()
                .flatMap(hasDeliveries -> {
                    if (hasDeliveries) {
                        return Mono.error(new IllegalStateException("Drone has active deliveries"));
                    }
                    return droneRepository.deleteById(id);
                });
    }
}
