package br.com.mottugrid_java.service;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import br.com.mottugrid_java.domainmodel.Sensor;
import br.com.mottugrid_java.dto.SensorRequestDto;
import br.com.mottugrid_java.dto.SensorResponseDto;
import br.com.mottugrid_java.exception.ResourceNotFoundException;
import br.com.mottugrid_java.repository.MotorcycleRepository;
import br.com.mottugrid_java.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final MotorcycleRepository motorcycleRepository;

    @Transactional
    public SensorResponseDto createSensor(SensorRequestDto dto) {
        Sensor sensor = mapToEntity(dto);
        sensorRepository.save(sensor);
        return mapToResponseDto(sensor);
    }

    @Transactional(readOnly = true)
    public SensorResponseDto getById(UUID id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found"));
        return mapToResponseDto(sensor);
    }

    @Transactional
    public SensorResponseDto updateSensor(UUID id, SensorRequestDto dto) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found"));
        sensor.setIsActive(dto.isActive());
        sensor.setBatteryLevel(dto.batteryLevel());
        sensor.setLastUpdate(dto.lastUpdate());

        if (dto.motorcycleId() != null) {
            Motorcycle moto = motorcycleRepository.findById(dto.motorcycleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found"));
            sensor.setMotorcycle(moto);
        } else {
            sensor.setMotorcycle(null);
        }

        return mapToResponseDto(sensorRepository.save(sensor));
    }

    @Transactional
    public void deleteSensor(UUID id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found"));
        sensorRepository.delete(sensor);
    }

    private Sensor mapToEntity(SensorRequestDto dto) {
        Sensor sensor = new Sensor();
        sensor.setIsActive(dto.isActive());
        sensor.setBatteryLevel(dto.batteryLevel());
        sensor.setLastUpdate(dto.lastUpdate());

        if (dto.motorcycleId() != null) {
            Motorcycle moto = motorcycleRepository.findById(dto.motorcycleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Motorcycle not found"));
            sensor.setMotorcycle(moto);
        }

        return sensor;
    }

    private SensorResponseDto mapToResponseDto(Sensor sensor) {
        return new SensorResponseDto(
                sensor.getId(),
                sensor.getIsActive(),
                sensor.getBatteryLevel(),
                sensor.getLastUpdate(),
                sensor.getMotorcycle() != null ? sensor.getMotorcycle().getId() : null
        );
    }
}
