package br.com.mottugrid_java.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Resposta de erro da API")
public record ErrorResponseDTO(
        @Schema(description = "Código de status HTTP")
        int status,

        @Schema(description = "Timestamp do erro")
        LocalDateTime timestamp,

        @Schema(description = "Detalhes do erro por campo")
        Map<String, String> errors

) {}