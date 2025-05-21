package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.SensorRequestDto;
import br.com.mottugrid_java.dto.SensorResponseDto;
import br.com.mottugrid_java.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
@Tag(name = "Sensor", description = "Operações relacionadas aos sensores")
public class SensorController {

    private final SensorService sensorService;

    @PostMapping
    @Operation(summary = "Criar um novo sensor")
    public ResponseEntity<SensorResponseDto> create(@RequestBody @Valid SensorRequestDto dto) {
        return ResponseEntity.ok(sensorService.createSensor(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sensor por ID")
    public ResponseEntity<SensorResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(sensorService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um sensor")
    public ResponseEntity<SensorResponseDto> update(@PathVariable UUID id,
                                                    @RequestBody @Valid SensorRequestDto dto) {
        return ResponseEntity.ok(sensorService.updateSensor(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um sensor")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        sensorService.deleteSensor(id);
        return ResponseEntity.noContent().build();
    }
}
