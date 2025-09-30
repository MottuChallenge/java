package br.com.mottugrid_java;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.dto.MotorcycleRequestDTO;
import br.com.mottugrid_java.dto.MotorcycleResponseDTO;
import br.com.mottugrid_java.repository.MotorcycleRepository;
import br.com.mottugrid_java.repository.YardRepository;
import br.com.mottugrid_java.service.MotorcycleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MotorcycleServiceTest {


    @Mock
    private MotorcycleRepository motorcycleRepository;

    @Mock
    private YardRepository yardRepository;

    @InjectMocks
    private MotorcycleService motorcycleService;

    @Test
    void deveCriarMotocicletaComSucesso() {
        UUID yardId = UUID.randomUUID();
        MotorcycleRequestDTO requestDTO = new MotorcycleRequestDTO("Honda Biz", "XYZ-1234", "Honda", 2023, yardId);

        Yard mockYard = new Yard();
        mockYard.setId(yardId);

        Motorcycle motorcycleSalva = new Motorcycle();
        motorcycleSalva.setId(UUID.randomUUID());
        motorcycleSalva.setPlate(requestDTO.plate());
        motorcycleSalva.setModel(requestDTO.model());
        motorcycleSalva.setYard(mockYard);

        when(motorcycleRepository.existsByPlate("XYZ-1234")).thenReturn(false);

        when(yardRepository.findById(yardId)).thenReturn(Optional.of(mockYard));

        when(motorcycleRepository.save(any(Motorcycle.class))).thenReturn(motorcycleSalva);

        MotorcycleResponseDTO resultado = motorcycleService.create(requestDTO);



        assertNotNull(resultado);
        assertEquals("Honda Biz", resultado.model());
        verify(motorcycleRepository, times(1)).save(any(Motorcycle.class));
    }

    @Test
    void deveMoverMotocicletaComSucesso() {
        UUID motorcycleId = UUID.randomUUID();
        UUID newYardId = UUID.randomUUID();

        Motorcycle motoExistente = new Motorcycle();
        motoExistente.setId(motorcycleId);
        motoExistente.setYard(new Yard());

        Yard novoPatio = new Yard();
        novoPatio.setId(newYardId);

        when(motorcycleRepository.findById(motorcycleId)).thenReturn(Optional.of(motoExistente));

        when(yardRepository.findById(newYardId)).thenReturn(Optional.of(novoPatio));

        motorcycleService.move(motorcycleId, newYardId);


        verify(motorcycleRepository, times(1)).save(motoExistente);

        assertEquals(newYardId, motoExistente.getYard().getId());
    }
}

