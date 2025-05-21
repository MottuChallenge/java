package br.com.mottugrid_java.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SensorResponseDto(
        UUID id,
        Boolean isActive,
        BigDecimal batteryLevel,
        LocalDateTime lastUpdate,
        UUID motorcycleId
) {}
