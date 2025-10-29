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

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

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



    @Test
    void create_shouldReturnSavedBranch() {

        when(branchRepository.save(any(Branch.class))).thenReturn(mockBranch);

        Branch result = branchService.create(mockBranch);

        assertNotNull(result);
        assertEquals(branchId, result.getId());
        assertEquals("Branch Teste", result.getName());
        verify(branchRepository, times(1)).save(mockBranch);
    }


    @Test
    void getById_shouldReturnBranchWhenFound() {
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(mockBranch));

        Branch result = branchService.getById(branchId);

        assertNotNull(result);
        assertEquals(branchId, result.getId());
        verify(branchRepository, times(1)).findById(branchId);
    }

    @Test
    void getById_shouldThrowExceptionWhenNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(branchRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> branchService.getById(nonExistentId));
        verify(branchRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void list_shouldCallFindAllWhenNameIsNull() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Branch> mockPage = new PageImpl<>(List.of(mockBranch));

        when(branchRepository.findAll(pageable)).thenReturn(mockPage);

        Page<Branch> result = branchService.list(null, pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(branchRepository, times(1)).findAll(pageable);
        verify(branchRepository, never()).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void list_shouldCallFindAllWhenNameIsBlank() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Branch> mockPage = new PageImpl<>(List.of(mockBranch));

        when(branchRepository.findAll(pageable)).thenReturn(mockPage);

        Page<Branch> result = branchService.list("  ", pageable);

        assertFalse(result.isEmpty());
        verify(branchRepository, times(1)).findAll(pageable);
        verify(branchRepository, never()).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void list_shouldCallFindByNameContainingIgnoreCaseWhenNameIsProvided() {
        String searchName = "Teste";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Branch> mockPage = new PageImpl<>(List.of(mockBranch));

        when(branchRepository.findByNameContainingIgnoreCase(searchName, pageable)).thenReturn(mockPage);

        Page<Branch> result = branchService.list(searchName, pageable);

        assertFalse(result.isEmpty());
        verify(branchRepository, times(1)).findByNameContainingIgnoreCase(searchName, pageable);
        verify(branchRepository, never()).findAll(any(Pageable.class));
    }



    @Test
    void update_shouldUpdateBranchWhenFound() {
        Branch newDetails = Branch.builder()
                .name("Branch Nova")
                .city("Cidade Nova")
                .state("NV")
                .phone("987654321")
                .build();

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(mockBranch));
        when(branchRepository.save(any(Branch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Branch result = branchService.update(branchId, newDetails);

        assertNotNull(result);
        assertEquals(branchId, result.getId());
        assertEquals("Branch Nova", result.getName());
        assertEquals("Cidade Nova", result.getCity());
        assertEquals("NV", result.getState());
        assertEquals("987654321", result.getPhone());
        verify(branchRepository, times(1)).findById(branchId);
        verify(branchRepository, times(1)).save(mockBranch);
    }

    @Test
    void update_shouldThrowExceptionWhenNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        Branch newDetails = Branch.builder().build();

        when(branchRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> branchService.update(nonExistentId, newDetails));
        verify(branchRepository, times(1)).findById(nonExistentId);
        verify(branchRepository, never()).save(any(Branch.class)); // Verifica que save não foi chamado
    }


    @Test
    void delete_shouldCallDeleteWhenFound() {
        when(branchRepository.existsById(branchId)).thenReturn(true);

        assertDoesNotThrow(() -> branchService.delete(branchId));

        verify(branchRepository, times(1)).existsById(branchId);
        verify(branchRepository, times(1)).deleteById(branchId);
    }

    @Test
    void delete_shouldThrowExceptionWhenNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(branchRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> branchService.delete(nonExistentId));

        verify(branchRepository, times(1)).existsById(nonExistentId);
        verify(branchRepository, never()).deleteById(any(UUID.class));
    }
}