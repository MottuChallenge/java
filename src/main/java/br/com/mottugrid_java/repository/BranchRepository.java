package br.com.mottugrid_java.repository;

import br.com.mottugrid_java.domainmodel.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {
    Page<Branch> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
