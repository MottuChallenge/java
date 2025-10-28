// br.com.mottugrid_java.repository.MotorcycleRepository.java (Adicionar estes métodos)
package br.com.mottugrid_java.repository;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, UUID> {

    boolean existsByPlate(String plate);

    // NOVO: Usa FETCH JOIN para carregar o Yard junto, resolvendo o LazyInitializationException
    @Query("SELECT m FROM Motorcycle m JOIN FETCH m.yard")
    Page<Motorcycle> findAllWithYard(Pageable pageable);

    // NOVO: FETCH JOIN para listagem com filtro
    @Query("SELECT m FROM Motorcycle m JOIN FETCH m.yard WHERE LOWER(m.model) LIKE LOWER(CONCAT('%', :model, '%'))")
    Page<Motorcycle> findByModelContainingIgnoreCaseWithYard(@Param("model") String model, Pageable pageable);
}