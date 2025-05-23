package br.com.mottugrid_java.service;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.dto.MotorcycleRequestDTO;
import br.com.mottugrid_java.dto.MotorcycleResponseDTO;
import br.com.mottugrid_java.repository.MotorcycleRepository;
import br.com.mottugrid_java.repository.YardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MotorcycleService {

    @Autowired
    private MotorcycleRepository motorcycleRepository;

    @Autowired
    private YardRepository yardRepository;

    @Transactional
    public MotorcycleResponseDTO create(MotorcycleRequestDTO dto) {
        if (motorcycleRepository.existsByPlate(dto.plate())) {
            throw new IllegalArgumentException("Já existe uma motocicleta com a placa " + dto.plate());
        }

        Yard yard = yardRepository.findById(dto.yardId())
                .orElseThrow(() -> new EntityNotFoundException("Yard não encontrada com id " + dto.yardId()));

        Motorcycle motorcycle = Motorcycle.builder()
                .model(dto.model())
                .plate(dto.plate())
                .manufacturer(dto.manufacturer())
                .year(dto.year())
                .yard(yard)
                .build();

        motorcycle = motorcycleRepository.save(motorcycle);
        return toResponse(motorcycle);
    }

    public MotorcycleResponseDTO getById(UUID id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle não encontrada com id " + id));
        return toResponse(motorcycle);
    }

    public Page<MotorcycleResponseDTO> list(String model, Pageable pageable) {
        Page<Motorcycle> page = (model == null || model.isBlank())
                ? motorcycleRepository.findAll(pageable)
                : motorcycleRepository.findByModelContainingIgnoreCase(model, pageable);
        return page.map(this::toResponse);
    }

    @Transactional
    public MotorcycleResponseDTO update(UUID id, MotorcycleRequestDTO dto) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle não encontrada com id " + id));

        motorcycle.setModel(dto.model());
        motorcycle.setPlate(dto.plate());
        motorcycle.setManufacturer(dto.manufacturer());
        motorcycle.setYear(dto.year());

        if (dto.yardId() != null && !dto.yardId().equals(motorcycle.getYard().getId())) {
            Yard newYard = yardRepository.findById(dto.yardId())
                    .orElseThrow(() -> new EntityNotFoundException("Yard não encontrada com id " + dto.yardId()));
            motorcycle.setYard(newYard);
        }

        motorcycle = motorcycleRepository.save(motorcycle);
        return toResponse(motorcycle);
    }

    @Transactional
    public void delete(UUID id) {
        if (!motorcycleRepository.existsById(id)) {
            throw new EntityNotFoundException("Motorcycle não encontrada com id " + id);
        }
        motorcycleRepository.deleteById(id);
    }

    private MotorcycleResponseDTO toResponse(Motorcycle motorcycle) {
        return new MotorcycleResponseDTO(
                motorcycle.getId(),
                motorcycle.getModel(),
                motorcycle.getPlate(),
                motorcycle.getManufacturer(),
                motorcycle.getYear(),
                motorcycle.getYard() != null ? motorcycle.getYard().getId() : null
        );
    }
}
