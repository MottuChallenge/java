package br.com.mottugrid_java.service;

import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.dto.YardRequestDTO;
import br.com.mottugrid_java.dto.YardResponseDTO;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.repository.BranchRepository;
import br.com.mottugrid_java.repository.YardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class YardService {

    @Autowired
    private YardRepository yardRepository;
    @Autowired
    private BranchRepository branchRepository;

    public YardResponseDTO create(YardRequestDTO dto) {
        Yard yard = toEntity(dto);
        return toResponse(yardRepository.save(yard));
    }

    public YardResponseDTO getById(Long id) {
        Yard yard = yardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Yard não encontrada com id " + id));
        return toResponse(yard);
    }

    public Page<YardResponseDTO> list(String name, Pageable pageable) {
        Page<Yard> page = (name == null || name.isBlank())
                ? yardRepository.findAll(pageable)
                : yardRepository.findByNameContainingIgnoreCase(name, pageable);
        return page.map(this::toResponse);
    }

    public YardResponseDTO update(Long id, YardRequestDTO dto) {
        Yard yard = yardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Yard não encontrada com id " + id));

        yard.setName(dto.name());
        // futuros campos: address, branch...

        return toResponse(yardRepository.save(yard));
    }

    public void delete(Long id) {
        if (!yardRepository.existsById(id)) {
            throw new EntityNotFoundException("Yard não encontrada com id " + id);
        }
        yardRepository.deleteById(id);
    }

    private Yard toEntity(YardRequestDTO dto) {
        Branch branch = branchRepository.findById(dto.branchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch não encontrada com id " + dto.branchId()));
        return Yard.builder()
                .name(dto.name())
                .branch(branch)
                .build();
    }

    private YardResponseDTO toResponse(Yard yard) {
        return new YardResponseDTO(
                yard.getId(),
                yard.getName(),
                yard.getBranch() != null ? yard.getBranch().getId() : null
        );
    }

}
