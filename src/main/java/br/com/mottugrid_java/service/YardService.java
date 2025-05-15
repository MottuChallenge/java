package br.com.mottugrid_java.service;

import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.dto.YardRequestDto;
import br.com.mottugrid_java.dto.YardResponseDto;
import br.com.mottugrid_java.exception.ResourceNotFoundException;
import br.com.mottugrid_java.repository.BranchRepository;
import br.com.mottugrid_java.repository.YardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class YardService {

    private final YardRepository yardRepository;
    private final BranchRepository branchRepository;

    @Transactional
    public YardResponseDto create(YardRequestDto dto) {
        Branch branch = branchRepository.findById(dto.branchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        Yard yard = new Yard();
        yard.setCode(dto.code());
        yard.setBranch(branch);

        yardRepository.save(yard);
        return mapToDto(yard);
    }

    @Transactional(readOnly = true)
    public YardResponseDto getById(UUID id) {
        Yard yard = yardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Yard not found"));
        return mapToDto(yard);
    }

    @Transactional(readOnly = true)
    public List<YardResponseDto> getAll() {
        return yardRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public YardResponseDto update(UUID id, YardRequestDto dto) {
        Yard yard = yardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Yard not found"));

        Branch branch = branchRepository.findById(dto.branchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        yard.setCode(dto.code());
        yard.setBranch(branch);
        yardRepository.save(yard);

        return mapToDto(yard);
    }

    @Transactional
    public void delete(UUID id) {
        Yard yard = yardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Yard not found"));
        yardRepository.delete(yard);
    }

    private YardResponseDto mapToDto(Yard yard) {
        return new YardResponseDto(
                yard.getId(),
                yard.getCode(),
                yard.getBranch().getId()
        );
    }
}
