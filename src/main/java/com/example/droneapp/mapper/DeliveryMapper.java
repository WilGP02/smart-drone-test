package com.example.droneapp.mapper;

import com.example.droneapp.dto.DeliveryDto;
import com.example.droneapp.entities.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeliveryMapper {
    DeliveryMapper INSTANCE = Mappers.getMapper(DeliveryMapper.class);

    Delivery toEntity(DeliveryDto dto);

    DeliveryDto toDto(Delivery entity);
}
