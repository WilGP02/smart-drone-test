package com.example.droneapp.mapper;

import com.example.droneapp.dto.DroneDto;
import com.example.droneapp.entities.Drone;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DroneMapper {
    DroneMapper INSTANCE = Mappers.getMapper(DroneMapper.class);

    Drone toEntity(DroneDto dto);

    DroneDto toDto(Drone entity);
}
