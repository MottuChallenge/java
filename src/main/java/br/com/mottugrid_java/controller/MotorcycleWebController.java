package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.domainmodel.Motorcycle;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.dto.MotorcycleRequestDTO;
import br.com.mottugrid_java.dto.MotorcycleResponseDTO;
import br.com.mottugrid_java.dto.YardResponseDTO;
import br.com.mottugrid_java.service.MotorcycleService;
import br.com.mottugrid_java.service.YardService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
// REMOVIDO: import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/motorcycles")
public class MotorcycleWebController {

    // 1. Injeção via Construtor (Best Practice)
    private final MotorcycleService motorcycleService;
    private final YardService yardService;

    public MotorcycleWebController(MotorcycleService motorcycleService, YardService yardService) {
        this.motorcycleService = motorcycleService;
        this.yardService = yardService;
    }

    @GetMapping
    public String listMotorcycles(
            @RequestParam(required = false) String modelFilter,
            @PageableDefault(size = 10, sort = "model") Pageable pageable,
            Model model) {

        // Service retorna Page<Motorcycle> (Entidade)
        Page<Motorcycle> entityPage = motorcycleService.list(modelFilter, pageable);

        // CONVERSÃO INLINE: Page<Motorcycle> -> Page<MotorcycleResponseDTO>
        Page<MotorcycleResponseDTO> responsePage = entityPage.map(motorcycle -> new MotorcycleResponseDTO(
                motorcycle.getId(),
                motorcycle.getModel(),
                motorcycle.getPlate(),
                motorcycle.getManufacturer(),
                motorcycle.getYear(),
                motorcycle.getYard() != null ? motorcycle.getYard().getId() : null,
                motorcycle.getYard() != null ? motorcycle.getYard().getName() : "N/A"
        ));

        model.addAttribute("page", responsePage);
        model.addAttribute("model", modelFilter);

        return "motorcycle/list";
    }

    // Método auxiliar para converter Yard Entity -> YardResponseDTO para comboboxes
    private List<YardResponseDTO> getYardsForForm() {
        // Service retorna Page<Yard> (Entidade)
        Page<Yard> yardEntityPage = yardService.list(null, Pageable.unpaged());

        // CONVERSÃO INLINE: Page<Yard> -> List<YardResponseDTO>
        return yardEntityPage.getContent().stream()
                .map(yard -> new YardResponseDTO(
                        yard.getId(),
                        yard.getName(),
                        yard.getBranch() != null ? yard.getBranch().getId() : null
                ))
                .collect(Collectors.toList());
    }


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("motorcycle", new MotorcycleRequestDTO(null, null, null, Year.now().getValue(), null));
        // Usa DTOs convertidos
        model.addAttribute("yards", getYardsForForm());
        model.addAttribute("pageTitle", "Nova Motocicleta");
        return "motorcycle/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Motorcycle motorcycle = motorcycleService.getById(id); // Service retorna Entidade

        // CONVERSÃO INLINE: Entity -> DTO de Request
        var requestDTO = new MotorcycleRequestDTO(
                motorcycle.getModel(),
                motorcycle.getPlate(),
                motorcycle.getManufacturer(),
                motorcycle.getYear(),
                motorcycle.getYard().getId()
        );

        model.addAttribute("motorcycle", requestDTO);
        model.addAttribute("motorcycleId", id);
        // Usa DTOs convertidos
        model.addAttribute("yards", getYardsForForm());
        model.addAttribute("pageTitle", "Editar Motocicleta");
        return "motorcycle/form";
    }

    @PostMapping("/save") // CORRIGIDO: Este mapeamento Post resolve o erro "No static resource motorcycles/save."
    public String saveMotorcycle(@Valid @ModelAttribute("motorcycle") MotorcycleRequestDTO dto,
                                 @RequestParam(value = "id", required = false) UUID id,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            // Recarrega DTOs para o formulário em caso de erro
            model.addAttribute("yards", getYardsForForm());
            model.addAttribute("pageTitle", id == null ? "Nova Motocicleta" : "Editar Motocicleta");
            return "motorcycle/form";
        }

        // CONVERSÃO INLINE: DTO -> Entidade antes de chamar o Service (Regra do Professor)
        Motorcycle motorcycleEntity = Motorcycle.builder()
                .model(dto.model())
                .plate(dto.plate())
                .manufacturer(dto.manufacturer())
                .year(dto.year())
                // Cria referência de Yard. Service fará a busca completa.
                .yard(Yard.builder().id(dto.yardId()).build())
                .build();

        try {
            if (id == null) {
                motorcycleService.create(motorcycleEntity);
                redirectAttributes.addFlashAttribute("message", "Motocicleta criada com sucesso!");
            } else {
                motorcycleService.update(id, motorcycleEntity);
                redirectAttributes.addFlashAttribute("message", "Motocicleta atualizada com sucesso!");
            }
        } catch (IllegalArgumentException e) {
            result.rejectValue("plate", "plate.duplicate", e.getMessage());
            // Recarrega DTOs para o formulário em caso de erro
            model.addAttribute("yards", getYardsForForm());
            model.addAttribute("pageTitle", id == null ? "Nova Motocicleta" : "Editar Motocicleta");
            return "motorcycle/form";
        }

        return "redirect:/motorcycles";
    }

    @GetMapping("/delete/{id}")
    public String deleteMotorcycle(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        motorcycleService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Motocicleta removida com sucesso!");
        return "redirect:/motorcycles";
    }

    @GetMapping("/move/{id}")
    public String showMoveForm(@PathVariable UUID id, Model model) {
        Motorcycle motorcycle = motorcycleService.getById(id);

        // CONVERSÃO INLINE: Entity -> DTO de Response para a view move
        MotorcycleResponseDTO responseDTO = new MotorcycleResponseDTO(
                motorcycle.getId(),
                motorcycle.getModel(),
                motorcycle.getPlate(),
                motorcycle.getManufacturer(),
                motorcycle.getYear(),
                motorcycle.getYard() != null ? motorcycle.getYard().getId() : null,
                motorcycle.getYard() != null ? motorcycle.getYard().getName() : "N/A"
        );

        model.addAttribute("motorcycle", responseDTO);
        model.addAttribute("yards", getYardsForForm());
        return "motorcycle/move";
    }

    @PostMapping("/move")
    public String moveMotorcycle(@RequestParam("motorcycleId") UUID motorcycleId,
                                 @RequestParam("yardId") UUID yardId,
                                 RedirectAttributes redirectAttributes) {
        try {
            motorcycleService.move(motorcycleId, yardId);
            redirectAttributes.addFlashAttribute("message", "Motocicleta movida com sucesso!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/motorcycles";
    }
}