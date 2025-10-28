// br.com.mottugrid_java.repository.MotorcycleRepository.java (Adicionar estes métodos)
package br.com.mottugrid_java.repository;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, UUID> {

    boolean existsByPlate(String plate);

    // Usa FETCH JOIN para carregar o Yard junto, resolvendo o LazyInitializationException
    @Query("SELECT m FROM Motorcycle m JOIN FETCH m.yard")
    Page<Motorcycle> findAllWithYard(Pageable pageable);

    // FETCH JOIN para listagem com filtro
    @Query("SELECT m FROM Motorcycle m JOIN FETCH m.yard WHERE LOWER(m.model) LIKE LOWER(CONCAT('%', :model, '%'))")
    Page<Motorcycle> findByModelContainingIgnoreCaseWithYard(@Param("model") String model, Pageable pageable);

    // NOVO: FETCH JOIN para carregar Motocicleta por ID junto com o Pátio
    @Query("SELECT m FROM Motorcycle m JOIN FETCH m.yard WHERE m.id = :id")
    Optional<Motorcycle> findByIdWithYard(@Param("id") UUID id);
}