package br.com.mottugrid_java.dto;

import java.util.UUID;

public record YardResponseDTO(
        UUID id,
        String name,
        UUID branchId
) {}
