package br.com.mottugrid_java.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "O nome de usuário não pode estar em branco")
        String username,

        @NotBlank(message = "A senha não pode estar em branco")
        String password
) {}
