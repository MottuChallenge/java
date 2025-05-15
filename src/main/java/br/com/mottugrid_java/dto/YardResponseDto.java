package br.com.mottugrid_java.dto;

import java.util.UUID;

public record YardResponseDto(
        UUID id,
        String code,
        UUID branchId
) {}
