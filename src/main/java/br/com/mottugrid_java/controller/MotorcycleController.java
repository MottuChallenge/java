package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.MotorcycleRequestDto;
import br.com.mottugrid_java.dto.MotorcycleResponseDto;
import br.com.mottugrid_java.service.MotorcycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/motorcycles")
@RequiredArgsConstructor
@Tag(name = "Motorcycle", description = "Operações relacionadas a motocicletas")
public class MotorcycleController {

    private final MotorcycleService motorcycleService;

    @PostMapping
    @Operation(summary = "Criar uma nova motocicleta")
    public ResponseEntity<MotorcycleResponseDto> create(
            @RequestBody @Valid MotorcycleRequestDto dto) {
        return ResponseEntity.ok(motorcycleService.createMotorcycle(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar motocicleta por ID")
    public ResponseEntity<MotorcycleResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(motorcycleService.getById(id));
    }

    @GetMapping("/yard/{yardId}")
    @Operation(summary = "Buscar todas as motocicletas de um pátio")
    public ResponseEntity<List<MotorcycleResponseDto>> getByYard(@PathVariable UUID yardId) {
        return ResponseEntity.ok(motorcycleService.getMotorcyclesByYard(yardId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma motocicleta")
    public ResponseEntity<MotorcycleResponseDto> update(
            @PathVariable UUID id,
            @RequestBody @Valid MotorcycleRequestDto dto) {
        return ResponseEntity.ok(motorcycleService.updateMotorcycle(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma motocicleta")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        motorcycleService.deleteMotorcycle(id);
        return ResponseEntity.noContent().build();
    }
}
