package br.com.mottugrid_java.infrastructure.runner;

import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.domainmodel.Motorcycle;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.repository.BranchRepository;
import br.com.mottugrid_java.repository.MotorcycleRepository;
import br.com.mottugrid_java.repository.YardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class CommandRunner {

    @Bean
    public CommandLineRunner seedDatabase(BranchRepository branchRepository, YardRepository yardRepository, MotorcycleRepository motorcycleRepository) {
        return args -> {
            List<String> modelosMottu = List.of(
                    "Honda Biz 110i",
                    "Honda Pop 110i",
                    "Yamaha Neo 125",
                    "Honda Elite 125",
                    "Yamaha Factor 125i",
                    "Honda CG 160 Start"
            );

            // Verifica se alguma das motos da Mottu já está cadastrada
            boolean motosJaExistem = motorcycleRepository.findAll().stream()
                    .anyMatch(moto -> modelosMottu.contains(moto.getModel()));

            if (motosJaExistem) {
                System.out.println("Dados de motos da Mottu já existem. Seed não será executado.");
                return;
            }

            List<String> cidades = List.of("São Paulo", "Rio de Janeiro", "Belo Horizonte", "Salvador", "Fortaleza", "Curitiba", "Manaus", "Recife", "Porto Alegre", "Brasília");
            List<String> estados = List.of("SP", "RJ", "MG", "BA", "CE", "PR", "AM", "PE", "RS", "DF");

            List<Branch> branches = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Branch branch = Branch.builder()
                        .name("Mottu " + cidades.get(i))
                        .city(cidades.get(i))
                        .state(estados.get(i))
                        .phone("(11) 9000-000" + i)
                        .build();
                branch = branchRepository.save(branch);
                branches.add(branch);
            }

            List<Yard> yards = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Yard yard = Yard.builder()
                        .name("Pátio " + (i + 1))
                        .branch(branches.get(i))
                        .build();
                yard = yardRepository.save(yard);
                yards.add(yard);
            }

            List<String> modelos = List.of(
                    "Honda Biz 110i",
                    "Honda Pop 110i",
                    "Yamaha Neo 125",
                    "Honda Elite 125",
                    "Yamaha Factor 125i",
                    "Honda CG 160 Start",
                    "Honda Biz 110i",
                    "Honda Pop 110i",
                    "Yamaha Neo 125",
                    "Honda CG 160 Start"
            );

            for (int i = 0; i < 10; i++) {
                Motorcycle moto = Motorcycle.builder()
                        .model(modelos.get(i))
                        .manufacturer(modelos.get(i).split(" ")[0]) // Honda ou Yamaha
                        .plate("ABC" + (1000 + i))
                        .year(2020 + (i % 4))
                        .yard(yards.get(new Random().nextInt(yards.size())))
                        .build();
                motorcycleRepository.save(moto);
            }

            System.out.println("Seed de dados da Mottu inserido com sucesso.");
        };
    }
}

