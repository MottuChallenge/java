package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.BranchRequestDTO;
import br.com.mottugrid_java.dto.BranchResponseDTO;
import br.com.mottugrid_java.service.BranchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
@Tag(name = "Branches", description = "Operações relacionadas às filiais")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping
    public ResponseEntity<Page<BranchResponseDTO>> list(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort sortOrder = Sort.by(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return ResponseEntity.ok(branchService.list(name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(branchService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BranchResponseDTO> create(@RequestBody @Valid BranchRequestDTO dto, UriComponentsBuilder uriComponentsBuilder) {
        BranchResponseDTO branchResponseDTO = branchService.create(dto);
        URI uri = uriComponentsBuilder.path("/api/branches/{id}").buildAndExpand(branchResponseDTO.id()).toUri();
        return ResponseEntity.created(uri).body(branchResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid BranchRequestDTO dto) {
        return ResponseEntity.ok(branchService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
