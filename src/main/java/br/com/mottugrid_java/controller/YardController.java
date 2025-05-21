package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.YardRequestDTO;
import br.com.mottugrid_java.dto.YardResponseDTO;
import br.com.mottugrid_java.service.YardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/yards")
public class YardController {

    @Autowired
    private YardService yardService;

    @Operation(summary = "Lista todos os yards com paginação, filtro opcional por nome e ordenação")
    @GetMapping
    public Page<YardResponseDTO> list(
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

        return yardService.list(name, pageable);
    }

    @Operation(summary = "Busca um yard pelo ID")
    @GetMapping("/{id}")
    public YardResponseDTO getById(@PathVariable Long id) {
        return yardService.getById(id);
    }

    @Operation(summary = "Cria um novo yard")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public YardResponseDTO create(@RequestBody @Valid YardRequestDTO dto) {
        return yardService.create(dto);
    }

    @Operation(summary = "Atualiza um yard existente pelo ID")
    @PutMapping("/{id}")
    public YardResponseDTO update(@PathVariable Long id, @RequestBody @Valid YardRequestDTO dto) {
        return yardService.update(id, dto);
    }

    @Operation(summary = "Remove um yard pelo ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        yardService.delete(id);
    }
}
