package br.com.mottugrid_java.service;

import br.com.mottugrid_java.dto.DashboardStatsDTO;
import br.com.mottugrid_java.repository.BranchRepository;
import br.com.mottugrid_java.repository.MotorcycleRepository;
import br.com.mottugrid_java.repository.YardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final BranchRepository branchRepository;
    private final YardRepository yardRepository;
    private final MotorcycleRepository motorcycleRepository;

    public DashboardService(BranchRepository branchRepository, YardRepository yardRepository, MotorcycleRepository motorcycleRepository) {
        this.branchRepository = branchRepository;
        this.yardRepository = yardRepository;
        this.motorcycleRepository = motorcycleRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        long totalBranches = branchRepository.count();
        long totalYards = yardRepository.count();
        long totalMotorcycles = motorcycleRepository.count();

        return new DashboardStatsDTO(totalBranches, totalYards, totalMotorcycles);
    }
}
