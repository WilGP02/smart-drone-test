package com.example.droneapp.unit;

import com.example.droneapp.dto.DeliveryDto;
import com.example.droneapp.dto.DroneDto;
import com.example.droneapp.entities.Delivery;
import com.example.droneapp.entities.Drone;
import com.example.droneapp.mapper.DeliveryMapper;
import com.example.droneapp.repository.DeliveryRepository;
import com.example.droneapp.repository.DroneRepository;
import com.example.droneapp.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DroneServiceTest {

    @InjectMocks
    private DroneService droneService;

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadDrone_ShouldThrowError_WhenExceedingMaxWeight() {
        UUID droneId = UUID.randomUUID();
        Drone drone = Drone.builder()
                .id(droneId)
                .serialNumber("12345")
                .model("ModelX")
                .maxWeight(100.0)
                .currentWeight(90.0)
                .status("AVAILABLE")
                .build();

        when(droneRepository.findById(droneId)).thenReturn(Mono.just(drone));

        Mono<DroneDto> result = droneService.loadDrone(droneId, 20.0);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Exceeds max weight"))
                .verify();
    }

    @Test
    void deliverPackage_ShouldChangeStateToAvailable_WhenDeliveryIsCompleted() {
        UUID droneId = UUID.randomUUID();
        Drone drone = Drone.builder()
                .id(droneId)
                .serialNumber("12345")
                .model("ModelX")
                .maxWeight(100.0)
                .currentWeight(50.0)
                .status("LOADING")
                .build();

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .weight(50.0)
                .description("Test delivery")
                .build();

        Delivery delivery = DeliveryMapper.INSTANCE.toEntity(deliveryDto);

        when(droneRepository.findById(droneId)).thenReturn(Mono.just(drone));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(Mono.just(delivery));
        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<DroneDto> result = droneService.deliverPackage(droneId, deliveryDto);

        StepVerifier.create(result)
                .expectNextMatches(updatedDrone -> updatedDrone.getStatus().equals("AVAILABLE"))
                .verifyComplete();
    }

    @Test
    void deliverPackage_ShouldLogDeliveryEvent() {
        UUID droneId = UUID.randomUUID();
        Drone drone = Drone.builder()
                .id(droneId)
                .serialNumber("12345")
                .model("ModelX")
                .maxWeight(100.0)
                .currentWeight(50.0)
                .status("LOADING")
                .build();

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .weight(50.0)
                .description("Test delivery")
                .build();

        Delivery delivery = DeliveryMapper.INSTANCE.toEntity(deliveryDto);

        when(droneRepository.findById(droneId)).thenReturn(Mono.just(drone));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(Mono.just(delivery));
        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<DroneDto> result = droneService.deliverPackage(droneId, deliveryDto);

        StepVerifier.create(result)
                .expectNextMatches(updatedDrone -> updatedDrone.getStatus().equals("AVAILABLE"))
                .verifyComplete();

        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }
}
