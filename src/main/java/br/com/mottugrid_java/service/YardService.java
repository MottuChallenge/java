package br.com.mottugrid_java.service;

import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.domainmodel.Yard;
// DTO IMPORTS REMOVIDOS
import br.com.mottugrid_java.repository.BranchRepository;
import br.com.mottugrid_java.repository.YardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
// @Autowired REMOVIDO
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"yards"})
public class YardService {

    private final YardRepository yardRepository;
    private final BranchRepository branchRepository;

    // Construtor para Injeção de Dependência (Best Practice)
    public YardService(YardRepository yardRepository, BranchRepository branchRepository) {
        this.yardRepository = yardRepository;
        this.branchRepository = branchRepository;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    // CREATE: Recebe Yard e retorna Yard
    public Yard create(Yard yard) {
        Branch branch = branchRepository.findById(yard.getBranch().getId())
                .orElseThrow(() -> new EntityNotFoundException("Branch não encontrada com id " + yard.getBranch().getId()));

        yard.setBranch(branch);
        return yardRepository.save(yard);
    }

    @Cacheable(key = "#id")
    // READ (by ID): Retorna Yard
    public Yard getById(UUID id) {
        return yardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Yard não encontrada com id " + id));
    }


    @Cacheable(key = "T(java.util.Objects).toString(#name, 'all') + '-' + (#pageable.isPaged() ? #pageable.getPageNumber() + '-' + #pageable.getPageSize() : 'unpaged') + '-' + #pageable.getSort()")
    // READ (paginated): Retorna Page<Yard>
    public Page<Yard> list(String name, Pageable pageable) {
        Page<Yard> page = (name == null || name.isBlank())
                ? yardRepository.findAll(pageable)
                : yardRepository.findByNameContainingIgnoreCase(name, pageable);
        return page; // Retorna Page<Yard>
    }


    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(allEntries = true)
    })
    // UPDATE: Recebe Yard e retorna Yard
    public Yard update(UUID id, Yard updatedYard) {
        Yard yard = yardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Yard não encontrada com id " + id));

        yard.setName(updatedYard.getName());

        if (updatedYard.getBranch().getId() != null && !updatedYard.getBranch().getId().equals(yard.getBranch().getId())) {
            Branch newBranch = branchRepository.findById(updatedYard.getBranch().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Branch não encontrada com id " + updatedYard.getBranch().getId()));
            yard.setBranch(newBranch);
        }

        return yardRepository.save(yard);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(allEntries = true)
    })
    public void delete(UUID id) {
        if (!yardRepository.existsById(id)) {
            throw new EntityNotFoundException("Yard não encontrada com id " + id);
        }
        yardRepository.deleteById(id);
    }

    // MÉTODOS toEntity e toResponse REMOVIDOS
}