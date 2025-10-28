package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.dto.BranchRequestDTO;
import br.com.mottugrid_java.dto.BranchResponseDTO;
import br.com.mottugrid_java.service.BranchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
@Tag(name = "Branches", description = "API para gerenciar filiais")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    public ResponseEntity<BranchResponseDTO> create(@Valid @RequestBody BranchRequestDTO dto) {
        Branch branch = toEntity(dto);

        Branch createdBranch = branchService.create(branch);

        BranchResponseDTO response = toResponse(createdBranch);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> getById(@PathVariable UUID id) {
        Branch branch = branchService.getById(id);

        BranchResponseDTO response = toResponse(branch);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BranchResponseDTO>> list(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Branch> entityPage = branchService.list(name, pageable);

        Page<BranchResponseDTO> responsePage = entityPage.map(this::toResponse);

        return ResponseEntity.ok(responsePage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> update(@PathVariable UUID id,
                                                    @Valid @RequestBody BranchRequestDTO dto) {
        Branch updatedBranch = toEntity(dto);

        Branch savedBranch = branchService.update(id, updatedBranch);

        BranchResponseDTO response = toResponse(savedBranch);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }


    // =================================================================
    // MÉTODOS DE CONVERSÃO DTO <-> ENTITY (LOCALIZADOS NO CONTROLLER)
    // =================================================================

    private Branch toEntity(BranchRequestDTO dto) {
        return Branch.builder()
                .name(dto.name())
                .city(dto.city())
                .state(dto.state())
                .phone(dto.phone())
                .build();
    }

    private BranchResponseDTO toResponse(Branch branch) {
        return new BranchResponseDTO(
                branch.getId(),
                branch.getName(),
                branch.getCity(),
                branch.getState(),
                branch.getPhone()
        );
    }
}