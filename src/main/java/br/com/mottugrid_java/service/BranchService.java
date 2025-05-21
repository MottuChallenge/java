package br.com.mottugrid_java.service;

import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.dto.BranchRequestDTO;
import br.com.mottugrid_java.dto.BranchResponseDTO;
import br.com.mottugrid_java.repository.BranchRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    // CREATE
    public BranchResponseDTO create(BranchRequestDTO dto) {
        Branch branch = toEntity(dto);
        return toResponse(branchRepository.save(branch));
    }

    // READ (by ID)
    public BranchResponseDTO getById(UUID id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch não encontrada com id " + id));
        return toResponse(branch);
    }

    // READ (paginated + optional filter by name)
    public Page<BranchResponseDTO> list(String name, Pageable pageable) {
        Page<Branch> page = (name == null || name.isBlank())
                ? branchRepository.findAll(pageable)
                : branchRepository.findByNameContainingIgnoreCase(name, pageable);

        return page.map(this::toResponse);
    }

    // UPDATE
    public BranchResponseDTO update(UUID id, BranchRequestDTO dto) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch não encontrada com id " + id));

        branch.setName(dto.name());
        branch.setCity(dto.city());
        branch.setState(dto.state());
        branch.setPhone(dto.phone());

        return toResponse(branchRepository.save(branch));
    }

    // DELETE
    public void delete(UUID id) {
        if (!branchRepository.existsById(id)) {
            throw new EntityNotFoundException("Branch não encontrada com id " + id);
        }
        branchRepository.deleteById(id);
    }

    // Converte DTO para entidade
    private Branch toEntity(BranchRequestDTO dto) {
        return Branch.builder()
                .name(dto.name())
                .city(dto.city())
                .state(dto.state())
                .phone(dto.phone())
                .build();
    }

    // Converte entidade para DTO
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
