package br.com.mottugrid_java.repository;

import br.com.mottugrid_java.domainmodel.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByIsResolvedFalse();
    List<Alert> findByMotorcycleId(UUID motorcycleId);
}