package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.YardRequestDto;
import br.com.mottugrid_java.dto.YardResponseDto;
import br.com.mottugrid_java.service.YardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/yards")
@RequiredArgsConstructor
@Tag(name = "Yard", description = "Operações relacionadas a pátios")
public class YardController {

    private final YardService yardService;

    @PostMapping
    @Operation(summary = "Criar um novo pátio")
    public ResponseEntity<YardResponseDto> create(@RequestBody @Valid YardRequestDto dto) {
        return ResponseEntity.ok(yardService.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pátio por ID")
    public ResponseEntity<YardResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(yardService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os pátios")
    public ResponseEntity<List<YardResponseDto>> getAll() {
        return ResponseEntity.ok(yardService.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um pátio")
    public ResponseEntity<YardResponseDto> update(@PathVariable UUID id, @RequestBody @Valid YardRequestDto dto) {
        return ResponseEntity.ok(yardService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um pátio")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        yardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
