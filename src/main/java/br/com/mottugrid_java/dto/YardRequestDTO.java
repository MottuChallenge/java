package br.com.mottugrid_java.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record YardRequestDTO(
        @NotBlank String name,
        @NotNull UUID branchId
) {}
