package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.BranchRequestDTO;
import br.com.mottugrid_java.dto.BranchResponseDTO;
import br.com.mottugrid_java.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping
    public Page<BranchResponseDTO> list(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort sortOrder = Sort.by(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return branchService.list(name, pageable);
    }

    @GetMapping("/{id}")
    public BranchResponseDTO getById(@PathVariable UUID id) {
        return branchService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BranchResponseDTO create(@RequestBody @Valid BranchRequestDTO dto) {
        return branchService.create(dto);
    }

    @PutMapping("/{id}")
    public BranchResponseDTO update(@PathVariable UUID id, @RequestBody @Valid BranchRequestDTO dto) {
        return branchService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        branchService.delete(id);
    }
}
