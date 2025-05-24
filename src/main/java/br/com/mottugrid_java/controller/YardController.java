package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.YardRequestDTO;
import br.com.mottugrid_java.dto.YardResponseDTO;
import br.com.mottugrid_java.service.YardService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/yards")
@Tag(name = "Yards", description = "Operações relacionadas aos pátios")
public class YardController {

    @Autowired
    private YardService yardService;

    @Operation(summary = "Lista todos os yards com paginação, filtro opcional por nome e ordenação")
    @GetMapping
    public ResponseEntity<Page<YardResponseDTO>> list(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        String sortField = "id";
        String sortDirection = "asc";

        if (sort.length == 2) {
            sortField = sort[0];
            sortDirection = sort[1];
        } else if (sort.length == 1) {
            sortField = sort[0];
        }

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sortOrder = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        return ResponseEntity.ok(yardService.list(name, pageable));
    }

    @Operation(summary = "Busca um yard pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<YardResponseDTO> getById(@PathVariable UUID id) {

        return ResponseEntity.ok(yardService.getById(id));
    }

    @Operation(summary = "Cria um novo yard")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<YardResponseDTO> create(@RequestBody @Valid YardRequestDTO dto, UriComponentsBuilder uriComponentsBuilder) {
        YardResponseDTO yardResponseDTO = yardService.create(dto);
        URI uri = uriComponentsBuilder.path("/api/yards/{id}").buildAndExpand(yardResponseDTO.id()).toUri();
        return ResponseEntity.created(uri).body(yardResponseDTO);
    }

    @Operation(summary = "Atualiza um yard existente pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<YardResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid YardRequestDTO dto) {
        return ResponseEntity.ok(yardService.update(id, dto));
    }

    @Operation(summary = "Remove um yard pelo ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        yardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
