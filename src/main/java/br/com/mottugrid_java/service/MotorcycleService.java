package br.com.mottugrid_java.service;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.repository.MotorcycleRepository;
import br.com.mottugrid_java.repository.YardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MotorcycleService {

    private final MotorcycleRepository motorcycleRepository;
    private final YardRepository yardRepository;

    public MotorcycleService(MotorcycleRepository motorcycleRepository, YardRepository yardRepository) {
        this.motorcycleRepository = motorcycleRepository;
        this.yardRepository = yardRepository;
    }


    @Transactional
    public Motorcycle create(Motorcycle motorcycle) {
        if (motorcycleRepository.existsByPlate(motorcycle.getPlate())) {
            throw new IllegalArgumentException("Já existe uma motocicleta com a placa " + motorcycle.getPlate());
        }

        Yard yard = yardRepository.findById(motorcycle.getYard().getId())
                .orElseThrow(() -> new EntityNotFoundException("Yard não encontrada com id " + motorcycle.getYard().getId()));

        motorcycle.setYard(yard);
        return motorcycleRepository.save(motorcycle);
    }

    @Transactional(readOnly = true)
    public Motorcycle getById(UUID id) {
        // CORREÇÃO AQUI: Usa o novo método do Repository que faz FETCH JOIN
        return motorcycleRepository.findByIdWithYard(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle não encontrada com id " + id));
    }

    @Transactional(readOnly = true)
    // Usa os métodos do Repository que fazem FETCH JOIN
    public Page<Motorcycle> list(String model, Pageable pageable) {
        return (model == null || model.isBlank())
                ? motorcycleRepository.findAllWithYard(pageable) // Usa FETCH JOIN
                : motorcycleRepository.findByModelContainingIgnoreCaseWithYard(model, pageable); // Usa FETCH JOIN
    }

    @Transactional
    public Motorcycle update(UUID id, Motorcycle updatedMotorcycle) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle não encontrada com id " + id));

        motorcycle.setModel(updatedMotorcycle.getModel());
        motorcycle.setPlate(updatedMotorcycle.getPlate());
        motorcycle.setManufacturer(updatedMotorcycle.getManufacturer());
        motorcycle.setYear(updatedMotorcycle.getYear());

        UUID newYardId = updatedMotorcycle.getYard() != null ? updatedMotorcycle.getYard().getId() : null;
        if (newYardId != null && !newYardId.equals(motorcycle.getYard().getId())) {
            Yard newYard = yardRepository.findById(newYardId)
                    .orElseThrow(() -> new EntityNotFoundException("Yard não encontrada com id " + newYardId));
            motorcycle.setYard(newYard);
        }

        return motorcycleRepository.save(motorcycle);
    }

    @Transactional
    public void delete(UUID id) {
        if (!motorcycleRepository.existsById(id)) {
            throw new EntityNotFoundException("Motorcycle não encontrada com id " + id);
        }
        motorcycleRepository.deleteById(id);
    }

    @Transactional
    public void move(UUID motorcycleId, UUID newYardId) {
        Motorcycle motorcycle = motorcycleRepository.findById(motorcycleId)
                .orElseThrow(() -> new EntityNotFoundException("Motocicleta não encontrada com id " + motorcycleId));

        Yard newYard = yardRepository.findById(newYardId)
                .orElseThrow(() -> new EntityNotFoundException("Pátio de destino não encontrado com id " + newYardId));

        motorcycle.setYard(newYard);
        motorcycleRepository.save(motorcycle);
    }
}