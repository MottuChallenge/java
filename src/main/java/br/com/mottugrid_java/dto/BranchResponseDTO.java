package br.com.mottugrid_java.dto;

import java.util.UUID;

public record BranchResponseDTO(
        UUID id,
        String name,
        String city,
        String state,
        String phone
) {}
