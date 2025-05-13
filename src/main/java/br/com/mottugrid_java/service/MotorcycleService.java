package br.com.mottugrid_java.service;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.domainmodel.enums.MotorcycleStatus;
import br.com.mottugrid_java.dto.MotorcycleRequestDto;
import br.com.mottugrid_java.dto.MotorcycleResponseDto;
import br.com.mottugrid_java.exception.ResourceNotFoundException;
import br.com.mottugrid_java.repository.MotorcycleRepository;
import br.com.mottugrid_java.repository.YardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MotorcycleService {

    private final MotorcycleRepository motorcycleRepository;
    private final YardRepository yardRepository;

    @Transactional
    public MotorcycleResponseDto createMotorcycle(MotorcycleRequestDto dto) {
        Motorcycle motorcycle = mapToEntity(dto);
        motorcycleRepository.save(motorcycle);
        return mapToResponseDto(motorcycle);
    }

    @Transactional(readOnly = true)
    public MotorcycleResponseDto getById(UUID id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found"));
        return mapToResponseDto(motorcycle);
    }

    @Transactional(readOnly = true)
    public List<MotorcycleResponseDto> getMotorcyclesByYard(UUID yardId) {
        return motorcycleRepository.findByYardId(yardId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Transactional
    public MotorcycleResponseDto updateMotorcycle(UUID id, MotorcycleRequestDto dto) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found with ID: " + id));

        motorcycle.setModel(dto.model());
        motorcycle.setBrand(dto.brand());
        motorcycle.setLicensePlate(dto.licensePlate());
        motorcycle.setEngineCapacity(dto.engineCapacity());
        motorcycle.setPrice(dto.price());

        if (dto.yardId() != null) {
            Yard yard = yardRepository.findById(dto.yardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Yard not found with ID: " + dto.yardId()));
            motorcycle.setYard(yard);
        } else {
            motorcycle.setYard(null);
        }

        motorcycleRepository.save(motorcycle);
        return mapToResponseDto(motorcycle);
    }

    @Transactional
    public void deleteMotorcycle(UUID id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found with ID: " + id));
        motorcycleRepository.delete(motorcycle);
    }


    private Motorcycle mapToEntity(MotorcycleRequestDto dto) {
        Motorcycle motorcycle = new Motorcycle();
        motorcycle.setModel(dto.model());
        motorcycle.setBrand(dto.brand());
        motorcycle.setLicensePlate(dto.licensePlate());
        motorcycle.setEngineCapacity(dto.engineCapacity());
        motorcycle.setPrice(dto.price());
        motorcycle.setStatus(MotorcycleStatus.AVAILABLE); // status padrão

        if (dto.yardId() != null) {
            Yard yard = yardRepository.findById(dto.yardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Yard not found with ID: " + dto.yardId()));
            motorcycle.setYard(yard);
        }

        return motorcycle;
    }

    private MotorcycleResponseDto mapToResponseDto(Motorcycle motorcycle) {
        return new MotorcycleResponseDto(
                motorcycle.getId(),
                motorcycle.getModel(),
                motorcycle.getBrand(),
                motorcycle.getLicensePlate(),
                motorcycle.getEngineCapacity(),
                motorcycle.getPrice(),
                motorcycle.getYard() != null ? motorcycle.getYard().getId() : null,
                motorcycle.getYard() != null ? motorcycle.getYard().getCode() : null
        );
    }
}
