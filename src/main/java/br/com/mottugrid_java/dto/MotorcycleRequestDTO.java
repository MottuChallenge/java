package br.com.mottugrid_java.dto;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record MotorcycleRequestDTO(

        @NotBlank(message = "O modelo não pode estar em branco")
        String model,

        @NotBlank(message = "A placa não pode estar em branco")
        String plate,

        @NotBlank(message = "O fabricante não pode estar em branco")
        String manufacturer,

        @NotNull(message = "O ano não pode ser nulo")
        @Min(value = 1900, message = "Ano inválido")
        @Max(value = 2100, message = "Ano inválido")
        Integer year,

        @NotNull(message = "O ID do pátio não pode ser nulo")
        UUID yardId
) {}
