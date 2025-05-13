package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.MotorcycleRequestDto;
import br.com.mottugrid_java.dto.MotorcycleResponseDto;
import br.com.mottugrid_java.service.MotorcycleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/motorcycles")
@RequiredArgsConstructor
public class MotorcycleController {

    private final MotorcycleService motorcycleService;

    @PostMapping
    public ResponseEntity<MotorcycleResponseDto> create(
            @RequestBody @Valid MotorcycleRequestDto dto) {
        return ResponseEntity.ok(motorcycleService.createMotorcycle(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotorcycleResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(motorcycleService.getById(id));
    }

    @GetMapping("/yard/{yardId}")
    public ResponseEntity<List<MotorcycleResponseDto>> getByYard(
            @PathVariable UUID yardId) {
        return ResponseEntity.ok(motorcycleService.getMotorcyclesByYard(yardId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotorcycleResponseDto> update(
            @PathVariable UUID id,
            @RequestBody @Valid MotorcycleRequestDto dto) {
        return ResponseEntity.ok(motorcycleService.updateMotorcycle(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        motorcycleService.deleteMotorcycle(id);
        return ResponseEntity.noContent().build();
    }
}
