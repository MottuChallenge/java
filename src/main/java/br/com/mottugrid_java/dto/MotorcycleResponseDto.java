package br.com.mottugrid_java.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record MotorcycleResponseDto(
        UUID id,
        String model,
        String brand,
        String licensePlate,
        Integer engineCapacity,
        BigDecimal price,
        UUID yardId,
        String yardCode
) {}