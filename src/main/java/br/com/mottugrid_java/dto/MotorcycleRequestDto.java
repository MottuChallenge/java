package br.com.mottugrid_java.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MotorcycleRequestDto {

    @NotBlank
    private String model;

    @NotBlank
    private String brand;

    @NotBlank
    private String licensePlate;

    @Positive
    private Integer engineCapacity;

    @Positive
    private BigDecimal price;

    private UUID yardId;
}
