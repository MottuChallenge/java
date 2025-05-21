package br.com.mottugrid_java.dto;


import jakarta.validation.constraints.NotBlank;

public record YardRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        String name
) {}
