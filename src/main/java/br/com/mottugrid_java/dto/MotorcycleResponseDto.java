package br.com.mottugrid_java.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MotorcycleResponseDto {

    private UUID id;
    private String model;
    private String brand;
    private String licensePlate;
    private Integer engineCapacity;
    private BigDecimal price;
    private UUID yardId;
    private String yardCode;
}
