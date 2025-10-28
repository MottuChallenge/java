package br.com.mottugrid_java.service;

import br.com.mottugrid_java.domainmodel.Branch;
// DTO IMPORTS REMOVIDOS
import br.com.mottugrid_java.repository.BranchRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
// @Autowired REMOVIDO
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
public class BranchService {

    private final BranchRepository branchRepository;

    // Construtor para Injeção de Dependência (Best Practice)
    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Transactional
    // CREATE: Recebe Branch e retorna Branch
    public Branch create(Branch branch) {
        return branchRepository.save(branch);
    }


    // READ (by ID): Retorna Branch
    public Branch getById(UUID id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch não encontrada com id " + id));
    }

    // READ (paginated): Retorna Page<Branch>
    public Page<Branch> list(String name, Pageable pageable) {
        return (name == null || name.isBlank())
                ? branchRepository.findAll(pageable)
                : branchRepository.findByNameContainingIgnoreCase(name, pageable);
    }
    @Transactional
    // UPDATE: Recebe Branch e retorna Branch
    public Branch update(UUID id, Branch updatedBranch) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch não encontrada com id " + id));

        branch.setName(updatedBranch.getName());
        branch.setCity(updatedBranch.getCity());
        branch.setState(updatedBranch.getState());
        branch.setPhone(updatedBranch.getPhone());

        return branchRepository.save(branch);
    }
    @Transactional
    // DELETE
    public void delete(UUID id) {
        if (!branchRepository.existsById(id)) {
            throw new EntityNotFoundException("Branch não encontrada com id " + id);
        }
        branchRepository.deleteById(id);
    }

    // MÉTODOS toEntity e toResponse REMOVIDOS
}