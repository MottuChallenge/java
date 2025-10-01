package br.com.mottugrid_java.dto;

import java.util.UUID;

public record MotorcycleResponseDTO(
        UUID id,
        String model,
        String plate,
        String manufacturer,
        Integer year,
        UUID yardId,
        String yardName
) {}

