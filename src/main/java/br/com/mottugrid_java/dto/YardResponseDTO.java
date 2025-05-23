package br.com.mottugrid_java.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record YardResponseDTO(

        @Schema(description = "Identificador único do pátio", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome do pátio", example = "Pátio Central")
        String name,

        @Schema(description = "ID da filial associada ao pátio", example = "b1c145b1-08b7-4f42-b9cc-cf1717e8a5b0")
        UUID branchId

) {}

