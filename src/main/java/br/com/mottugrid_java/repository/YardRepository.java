package br.com.mottugrid_java.repository;



import br.com.mottugrid_java.domainmodel.Yard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YardRepository extends JpaRepository<Yard, Long> {

    // Busca por nome contendo (case insensitive), com paginação
    Page<Yard> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

