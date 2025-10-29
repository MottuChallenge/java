package br.com.mottugrid_java;


import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.repository.BranchRepository;
import br.com.mottugrid_java.service.BranchService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe BranchService.
 */
@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {

    // Mock do repositório para isolar a camada de serviço da camada de dados
    @Mock
    private BranchRepository branchRepository;

    // Injeta os mocks no BranchService a ser testado
    @InjectMocks
    private BranchService branchService;

    private UUID branchId;
    private Branch mockBranch;

    @BeforeEach
    void setUp() {
        branchId = UUID.randomUUID();
        mockBranch = Branch.builder()
                .id(branchId)
                .name("Branch Teste")
                .city("Cidade Teste")
                .state("ST")
                .phone("123456789")
                .build();
    }

    // --- Testes para create(Branch branch) ---

    @Test
    void create_shouldReturnSavedBranch() {
        // Configuração do mock: quando save for chamado, retorna o mockBranch
        when(branchRepository.save(any(Branch.class))).thenReturn(mockBranch);

        Branch result = branchService.create(mockBranch);

        // Verificação
        assertNotNull(result);
        assertEquals(branchId, result.getId());
        assertEquals("Branch Teste", result.getName());
        // Verifica se o método save do repositório foi chamado exatamente uma vez
        verify(branchRepository, times(1)).save(mockBranch);
    }

    // --- Testes para getById(UUID id) ---

    @Test
    void getById_shouldReturnBranchWhenFound() {
        // Configuração do mock: quando findById for chamado com o ID, retorna um Optional contendo a branch
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(mockBranch));

        Branch result = branchService.getById(branchId);

        // Verificação
        assertNotNull(result);
        assertEquals(branchId, result.getId());
        verify(branchRepository, times(1)).findById(branchId);
    }

    @Test
    void getById_shouldThrowExceptionWhenNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        // Configuração do mock: quando findById for chamado, retorna um Optional vazio
        when(branchRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Verificação: espera-se que lance EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> branchService.getById(nonExistentId));
        verify(branchRepository, times(1)).findById(nonExistentId);
    }

    // --- Testes para list(String name, Pageable pageable) ---

    @Test
    void list_shouldCallFindAllWhenNameIsNull() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Branch> mockPage = new PageImpl<>(List.of(mockBranch));

        // Configuração do mock: quando findAll for chamado sem nome, retorna a página mockada
        when(branchRepository.findAll(pageable)).thenReturn(mockPage);

        Page<Branch> result = branchService.list(null, pageable);

        // Verificação
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        // Verifica se findAll foi chamado e findByNameContainingIgnoreCase não foi
        verify(branchRepository, times(1)).findAll(pageable);
        verify(branchRepository, never()).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void list_shouldCallFindAllWhenNameIsBlank() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Branch> mockPage = new PageImpl<>(List.of(mockBranch));

        // Configuração do mock: quando findAll for chamado com nome em branco, retorna a página mockada
        when(branchRepository.findAll(pageable)).thenReturn(mockPage);

        Page<Branch> result = branchService.list("  ", pageable);

        // Verificação
        assertFalse(result.isEmpty());
        // Verifica se findAll foi chamado
        verify(branchRepository, times(1)).findAll(pageable);
        verify(branchRepository, never()).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void list_shouldCallFindByNameContainingIgnoreCaseWhenNameIsProvided() {
        String searchName = "Teste";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Branch> mockPage = new PageImpl<>(List.of(mockBranch));

        // Configuração do mock: quando findByNameContainingIgnoreCase for chamado, retorna a página mockada
        when(branchRepository.findByNameContainingIgnoreCase(searchName, pageable)).thenReturn(mockPage);

        Page<Branch> result = branchService.list(searchName, pageable);

        // Verificação
        assertFalse(result.isEmpty());
        // Verifica se findByNameContainingIgnoreCase foi chamado
        verify(branchRepository, times(1)).findByNameContainingIgnoreCase(searchName, pageable);
        verify(branchRepository, never()).findAll(any(Pageable.class));
    }

    // --- Testes para update(UUID id, Branch updatedBranch) ---

    @Test
    void update_shouldUpdateBranchWhenFound() {
        Branch newDetails = Branch.builder()
                .name("Branch Nova")
                .city("Cidade Nova")
                .state("NV")
                .phone("987654321")
                .build();

        // 1. Configurar findById para retornar a branch existente
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(mockBranch));
        // 2. Configurar save para retornar a branch com os detalhes atualizados
        when(branchRepository.save(any(Branch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Branch result = branchService.update(branchId, newDetails);

        // Verificação
        assertNotNull(result);
        assertEquals(branchId, result.getId());
        assertEquals("Branch Nova", result.getName()); // Verifica se o nome foi atualizado
        assertEquals("Cidade Nova", result.getCity()); // Verifica se a cidade foi atualizada
        assertEquals("NV", result.getState()); // Verifica se o estado foi atualizado
        assertEquals("987654321", result.getPhone()); // Verifica se o telefone foi atualizado
        verify(branchRepository, times(1)).findById(branchId);
        verify(branchRepository, times(1)).save(mockBranch); // Verifica se o save foi chamado
    }

    @Test
    void update_shouldThrowExceptionWhenNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        Branch newDetails = Branch.builder().build();

        // Configuração do mock: findById retorna Optional vazio
        when(branchRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Verificação: espera-se que lance EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> branchService.update(nonExistentId, newDetails));
        verify(branchRepository, times(1)).findById(nonExistentId);
        verify(branchRepository, never()).save(any(Branch.class)); // Verifica que save não foi chamado
    }

    // --- Testes para delete(UUID id) ---

    @Test
    void delete_shouldCallDeleteWhenFound() {
        // 1. Configurar existsById para retornar true
        when(branchRepository.existsById(branchId)).thenReturn(true);
        // 2. Não há retorno para deleteById

        // Execução
        assertDoesNotThrow(() -> branchService.delete(branchId));

        // Verificação
        verify(branchRepository, times(1)).existsById(branchId);
        verify(branchRepository, times(1)).deleteById(branchId); // Verifica se o deleteById foi chamado
    }

    @Test
    void delete_shouldThrowExceptionWhenNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        // Configuração do mock: existsById retorna false
        when(branchRepository.existsById(nonExistentId)).thenReturn(false);

        // Verificação: espera-se que lance EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> branchService.delete(nonExistentId));

        verify(branchRepository, times(1)).existsById(nonExistentId);
        verify(branchRepository, never()).deleteById(any(UUID.class)); // Verifica que deleteById não foi chamado
    }
}