package br.com.mottugrid_java.repository;

import br.com.mottugrid_java.domainmodel.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SensorRepository extends JpaRepository<Sensor, UUID> {
}
