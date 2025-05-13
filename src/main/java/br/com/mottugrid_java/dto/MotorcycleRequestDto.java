package br.com.mottugrid_java.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record MotorcycleRequestDto(
        @NotBlank String model,
        @NotBlank String brand,
        @NotBlank String licensePlate,
        @Positive Integer engineCapacity,
        @Positive BigDecimal price,
        UUID yardId
) {}