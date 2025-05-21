package br.com.mottugrid_java.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SensorRequestDto(
        @NotNull Boolean isActive,
        @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal batteryLevel,
        @NotNull LocalDateTime lastUpdate,
        UUID motorcycleId
) {}
