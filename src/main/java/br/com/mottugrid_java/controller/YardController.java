package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.dto.YardRequestDTO;
import br.com.mottugrid_java.dto.YardResponseDTO;
import br.com.mottugrid_java.service.YardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    private final YardService yardService;

    public YardController(YardService yardService) {
        this.yardService = yardService;
    }

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

        Page<Yard> entityPage = yardService.list(name, pageable);

        Page<YardResponseDTO> responsePage = entityPage.map(this::toResponse);

        return ResponseEntity.ok(responsePage);
    }

    @Operation(summary = "Busca um yard pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<YardResponseDTO> getById(@PathVariable UUID id) {
        Yard yard = yardService.getById(id);

        return ResponseEntity.ok(toResponse(yard));
    }

    @Operation(summary = "Cria um novo yard")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<YardResponseDTO> create(@RequestBody @Valid YardRequestDTO dto, UriComponentsBuilder uriComponentsBuilder) {
        Yard yard = toEntity(dto);

        Yard createdYard = yardService.create(yard);

        YardResponseDTO yardResponseDTO = toResponse(createdYard);

        URI uri = uriComponentsBuilder.path("/api/yards/{id}").buildAndExpand(yardResponseDTO.id()).toUri();
        return ResponseEntity.created(uri).body(yardResponseDTO);
    }

    @Operation(summary = "Atualiza um yard existente pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<YardResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid YardRequestDTO dto) {
        Yard updatedYard = toEntity(dto);

        Yard savedYard = yardService.update(id, updatedYard);

        return ResponseEntity.ok(toResponse(savedYard));
    }

    @Operation(summary = "Remove um yard pelo ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        yardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =================================================================
    // MÉTODOS DE CONVERSÃO DTO <-> ENTITY (LOCALIZADOS NO CONTROLLER)
    // =================================================================

    private Yard toEntity(YardRequestDTO dto) {
        Branch branchReference = Branch.builder().id(dto.branchId()).build();

        Yard yard = Yard.builder()
                .name(dto.name())
                .build();

        yard.setBranch(branchReference);
        return yard;
    }

    private YardResponseDTO toResponse(Yard yard) {
        return new YardResponseDTO(
                yard.getId(),
                yard.getName(),
                yard.getBranch() != null ? yard.getBranch().getId() : null
        );
    }
}