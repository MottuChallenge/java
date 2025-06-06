package br.com.mottugrid_java.service;

import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.dto.YardRequestDTO;
import br.com.mottugrid_java.dto.YardResponseDTO;
import br.com.mottugrid_java.repository.BranchRepository;
import br.com.mottugrid_java.repository.YardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"yards"})
public class YardService {

    @Autowired
    private YardRepository yardRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Transactional
    @CacheEvict(allEntries = true)
    public YardResponseDTO create(YardRequestDTO dto) {
        Yard yard = toEntity(dto);
        return toResponse(yardRepository.save(yard));
    }

    @Cacheable(key = "#id")
    public YardResponseDTO getById(UUID id) {
        Yard yard = yardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Yard não encontrada com id " + id));
        return toResponse(yard);
    }

    @Cacheable(key = "#name != null && !#name.isBlank() ? #name + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString() : 'all-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<YardResponseDTO> list(String name, Pageable pageable) {
        Page<Yard> page = (name == null || name.isBlank())
                ? yardRepository.findAll(pageable)
                : yardRepository.findByNameContainingIgnoreCase(name, pageable);
        return page.map(this::toResponse);
    }


    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(allEntries = true)
    })
    public YardResponseDTO update(UUID id, YardRequestDTO dto) {
        Yard yard = yardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Yard não encontrada com id " + id));

        yard.setName(dto.name());

        if (dto.branchId() != null && !dto.branchId().equals(yard.getBranch().getId())) {
            Branch newBranch = branchRepository.findById(dto.branchId())
                    .orElseThrow(() -> new EntityNotFoundException("Branch não encontrada com id " + dto.branchId()));
            yard.setBranch(newBranch);
        }

        return toResponse(yardRepository.save(yard));
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

    private Yard toEntity(YardRequestDTO dto) {
        Branch branch = branchRepository.findById(dto.branchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch não encontrada com id " + dto.branchId()));

        Yard yard = Yard.builder()
                .name(dto.name())
                .build();

        yard.setBranch(branch);
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
