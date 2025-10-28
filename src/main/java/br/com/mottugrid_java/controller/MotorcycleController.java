// src/main/java/br/com/mottugrid_java/controller/MotorcycleController.java
package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.MotorcycleRequestDTO;
import br.com.mottugrid_java.dto.MotorcycleResponseDTO;
import br.com.mottugrid_java.service.MotorcycleService;
import br.com.mottugrid_java.domainmodel.Motorcycle; // Novo: Importar o Domain Model
import br.com.mottugrid_java.domainmodel.Yard; // Novo: Importar o Yard

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

    // 1. Injeção de Dependência: Usando 'final'
    private final MotorcycleService motorcycleService;


    public MotorcycleController(MotorcycleService motorcycleService) {
        this.motorcycleService = motorcycleService;
    }

    @PostMapping
    public ResponseEntity<MotorcycleResponseDTO> create(@Valid @RequestBody MotorcycleRequestDTO dto) {
        // 2. O Controller converte DTO -> Entidade
        Motorcycle motorcycle = toEntity(dto);

        // 3. O Service executa a lógica e retorna a Entidade
        Motorcycle createdMotorcycle = motorcycleService.create(motorcycle);

        // 4. O Controller converte Entidade -> DTO de resposta
        MotorcycleResponseDTO response = toResponse(createdMotorcycle);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotorcycleResponseDTO> getById(@PathVariable UUID id) {
        // 2. Service retorna a Entidade
        Motorcycle motorcycle = motorcycleService.getById(id);

        // 3. O Controller converte Entidade -> DTO de resposta
        MotorcycleResponseDTO response = toResponse(motorcycle);

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

        // 2. Service retorna Page<Entidade>
        Page<Motorcycle> entityPage = motorcycleService.list(model, pageable);

        // 3. O Controller converte Page<Entidade> para Page<DTO>
        Page<MotorcycleResponseDTO> responsePage = entityPage.map(this::toResponse);

        return ResponseEntity.ok(responsePage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotorcycleResponseDTO> update(@PathVariable UUID id,
                                                        @Valid @RequestBody MotorcycleRequestDTO dto) {
        Motorcycle updatedMotorcycle = toEntity(dto);

        Motorcycle savedMotorcycle = motorcycleService.update(id, updatedMotorcycle);

        MotorcycleResponseDTO response = toResponse(savedMotorcycle);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        motorcycleService.delete(id);
        return ResponseEntity.noContent().build();
    }


    // =================================================================
    // MÉTODOS DE CONVERSÃO DTO <-> ENTITY (LOCALIZADOS NO CONTROLLER)
    // =================================================================

    private Motorcycle toEntity(MotorcycleRequestDTO dto) {
        Yard yardReference = Yard.builder().id(dto.yardId()).build();

        return Motorcycle.builder()
                .model(dto.model())
                .plate(dto.plate())
                .manufacturer(dto.manufacturer())
                .year(dto.year())
                .yard(yardReference)
                .build();
    }

    private MotorcycleResponseDTO toResponse(Motorcycle motorcycle) {
        String yardName = (motorcycle.getYard() != null) ? motorcycle.getYard().getName() : "N/A";
        UUID yardId = (motorcycle.getYard() != null) ? motorcycle.getYard().getId() : null;

        return new MotorcycleResponseDTO(
                motorcycle.getId(),
                motorcycle.getModel(),
                motorcycle.getPlate(),
                motorcycle.getManufacturer(),
                motorcycle.getYear(),
                yardId,
                yardName
        );
    }
}