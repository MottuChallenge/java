package br.com.mottugrid_java.dto;

public record LoginResponseDTO(
        String token,
        String type,
        String role
) {}
