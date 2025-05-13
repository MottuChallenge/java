package br.com.mottugrid_java.repository;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, UUID> {

    @Query("SELECT m FROM Motorcycle m WHERE m.yard.id = :yardId")
    List<Motorcycle> findByYardId(@Param("yardId") UUID yardId);

    List<Motorcycle> findByLicensePlate(String licensePlate);

    @Query("SELECT m FROM Motorcycle m WHERE m.status = 'AVAILABLE'")
    List<Motorcycle> findAvailableMotorcycles();
}