package com.example.droneapp.controller;

import com.example.droneapp.dto.DeliveryDto;
import com.example.droneapp.dto.DroneDto;
import com.example.droneapp.dto.LoadRequest;
import com.example.droneapp.service.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/drones")
@Tag(name = "Drone API", description = "API for managing a fleet of delivery drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @Operation(
            summary = "Create a new drone",
            description = "Registers a new drone in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Drone created successfully",
                            content = @Content(schema = @Schema(implementation = DroneDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PostMapping
    public Mono<ResponseEntity<DroneDto>> createDrone(
            @RequestBody(description = "Drone data", required = true,
                    content = @Content(schema = @Schema(implementation = DroneDto.class)))
            Mono<DroneDto> droneDtoMono) {
        return droneService.createDrone(droneDtoMono)
                .map(drone -> ResponseEntity.status(201).body(drone));
    }

    @Operation(
            summary = "Get all drones",
            description = "Fetch a list of all drones with their details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Drones retrieved successfully")
            }
    )
    @GetMapping
    public Flux<DroneDto> getAllDrones() {
        return droneService.getAllDrones();
    }

    @Operation(
            summary = "Get drone by ID",
            description = "Fetch details of a specific drone by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Drone found",
                            content = @Content(schema = @Schema(implementation = DroneDto.class))),
                    @ApiResponse(responseCode = "404", description = "Drone not found")
            }
    )
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DroneDto>> getDroneById(
            @PathVariable UUID id) {
        return droneService.getDroneById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Load a drone",
            description = "Load a drone with a specific weight",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Drone loaded successfully",
                            content = @Content(schema = @Schema(implementation = DroneDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request or exceeds weight capacity")
            }
    )
    @PostMapping("/{id}/load")
    public Mono<ResponseEntity<DroneDto>> loadDrone(
            @PathVariable UUID id,
            @RequestBody(description = "Load request with weight", required = true,
                    content = @Content(schema = @Schema(implementation = LoadRequest.class)))
            LoadRequest loadRequest) {
        return droneService.loadDrone(id, loadRequest.getWeight())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Deliver a package",
            description = "Register a package delivery for a drone",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Delivery registered successfully",
                            content = @Content(schema = @Schema(implementation = DroneDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid delivery request")
            }
    )
    @PostMapping("/{id}/deliver")
    public Mono<ResponseEntity<DroneDto>> deliverPackage(
            @PathVariable UUID id,
            @RequestBody(description = "Delivery details", required = true,
                    content = @Content(schema = @Schema(implementation = DeliveryDto.class)))
            DeliveryDto deliveryDto) {
        return droneService.deliverPackage(id, deliveryDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(
            summary = "Get drone deliveries",
            description = "Fetch the delivery history for a specific drone",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deliveries retrieved successfully")
            }
    )
    @GetMapping("/{id}/deliveries")
    public Flux<DeliveryDto> getDroneDeliveries(
            @PathVariable UUID id) {
        return droneService.getDroneDeliveries(id);
    }

    @Operation(
            summary = "Delete a drone",
            description = "Delete a specific drone if it has no active deliveries",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Drone deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Cannot delete drone with active deliveries")
            }
    )
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteDrone(
            @PathVariable UUID id) {
        return droneService.deleteDrone(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
