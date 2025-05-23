package br.com.mottugrid_java.repository;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, UUID> {

    Page<Motorcycle> findByModelContainingIgnoreCase(String model, Pageable pageable);
    boolean existsByPlate(String plate);
}
