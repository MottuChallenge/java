package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.dto.MotorcycleRequestDTO;
import br.com.mottugrid_java.service.MotorcycleService;
import br.com.mottugrid_java.service.YardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MotorcycleWebControllerTest {

    private MockMvc mockMvc;
    private MotorcycleService motorcycleService;
    private YardService yardService;
    private MotorcycleWebController controller;

    @BeforeEach
    void setup() {
        motorcycleService = mock(MotorcycleService.class);
        yardService = mock(YardService.class);
        controller = new MotorcycleWebController(motorcycleService, yardService);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        // Mock básico do service para listagem
        UUID yardId = UUID.randomUUID();
        Motorcycle motorcycle = Motorcycle.builder()
                .id(UUID.randomUUID())
                .model("ModelX")
                .plate("ABC-1234")
                .manufacturer("Maker")
                .year(2020)
                .yard(Yard.builder().id(yardId).name("Main Yard").build())
                .build();

        when(motorcycleService.list(any(), any()))
                .thenReturn(new PageImpl<>(List.of(motorcycle), PageRequest.of(0, 10), 1));

        // Mock do service para editar/obter por id
        when(motorcycleService.getById(any(UUID.class))).thenReturn(motorcycle);

        when(yardService.list(any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
    }

    @Test
    void testListMotorcycles() throws Exception {
        mockMvc.perform(get("/motorcycles")
                        .param("page", "0")
                        .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("motorcycle/list"))
                .andExpect(model().attributeExists("page"));

        verify(motorcycleService, times(1)).list(any(), any());
    }

    @Test
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/motorcycles/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("motorcycle/form"))
                .andExpect(model().attributeExists("motorcycle"))
                .andExpect(model().attributeExists("yards"))
                .andExpect(model().attributeExists("pageTitle"));
    }

    @Test
    void testShowEditForm() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/motorcycles/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("motorcycle/form"))
                .andExpect(model().attributeExists("motorcycle"))
                .andExpect(model().attributeExists("yards"))
                .andExpect(model().attributeExists("pageTitle"));

        verify(motorcycleService, times(1)).getById(id);
    }

    @Test
    void testSaveMotorcycle_Create() throws Exception {
        MotorcycleRequestDTO dto = new MotorcycleRequestDTO("ModelX", "ABC-1234", "Maker", 2020, UUID.randomUUID());

        mockMvc.perform(post("/motorcycles/save")
                        .param("model", dto.model())
                        .param("plate", dto.plate())
                        .param("manufacturer", dto.manufacturer())
                        .param("year", String.valueOf(dto.year()))
                        .param("yardId", dto.yardId().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motorcycles"));

        verify(motorcycleService, times(1)).create(any(Motorcycle.class));
    }

    @Test
    void testSaveMotorcycle_Update() throws Exception {
        UUID id = UUID.randomUUID();
        MotorcycleRequestDTO dto = new MotorcycleRequestDTO("ModelZ", "DEF-5678", "MakerZ", 2022, UUID.randomUUID());

        mockMvc.perform(post("/motorcycles/save")
                        .param("id", id.toString())
                        .param("model", dto.model())
                        .param("plate", dto.plate())
                        .param("manufacturer", dto.manufacturer())
                        .param("year", String.valueOf(dto.year()))
                        .param("yardId", dto.yardId().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motorcycles"));

        verify(motorcycleService, times(1)).update(eq(id), any(Motorcycle.class));
    }

    @Test
    void testDeleteMotorcycle() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/motorcycles/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/motorcycles"));

        verify(motorcycleService, times(1)).delete(id);
    }
}
