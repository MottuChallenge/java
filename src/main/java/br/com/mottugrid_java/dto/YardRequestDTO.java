package br.com.mottugrid_java.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record YardRequestDTO(

        @NotBlank(message = "O nome do pátio é obrigatório")
        String name,

        @NotNull(message = "O ID da filial é obrigatório")
        UUID branchId

) {}
