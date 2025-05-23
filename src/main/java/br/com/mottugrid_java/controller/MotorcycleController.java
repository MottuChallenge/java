package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.MotorcycleRequestDTO;
import br.com.mottugrid_java.dto.MotorcycleResponseDTO;
import br.com.mottugrid_java.service.MotorcycleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/motorcycles")
@Tag(name = "Motorcycles", description = "API para gerenciar motocicletas")
public class MotorcycleController {

    private final MotorcycleService motorcycleService;


    public MotorcycleController(MotorcycleService motorcycleService) {
        this.motorcycleService = motorcycleService;
    }

    @PostMapping
    public ResponseEntity<MotorcycleResponseDTO> create(@Valid @RequestBody MotorcycleRequestDTO dto) {
        MotorcycleResponseDTO response = motorcycleService.create(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotorcycleResponseDTO> getById(@PathVariable UUID id) {
        MotorcycleResponseDTO response = motorcycleService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<MotorcycleResponseDTO>> list(
            @RequestParam(required = false) String model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "model") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MotorcycleResponseDTO> responsePage = motorcycleService.list(model, pageable);
        return ResponseEntity.ok(responsePage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotorcycleResponseDTO> update(@PathVariable UUID id,
                                                        @Valid @RequestBody MotorcycleRequestDTO dto) {
        MotorcycleResponseDTO response = motorcycleService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        motorcycleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
