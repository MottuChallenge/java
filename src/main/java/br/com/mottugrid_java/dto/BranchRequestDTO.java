package br.com.mottugrid_java.dto;

import jakarta.validation.constraints.NotBlank;

public record BranchRequestDTO(
        @NotBlank String name,
        @NotBlank String city,
        @NotBlank String state,
        String phone
) {}
