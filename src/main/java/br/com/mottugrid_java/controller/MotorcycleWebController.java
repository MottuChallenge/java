package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.MotorcycleRequestDTO;
import br.com.mottugrid_java.service.MotorcycleService;
import br.com.mottugrid_java.service.YardService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Year;
import java.util.UUID;

@Controller
@RequestMapping("/motorcycles")
public class MotorcycleWebController {

    @Autowired
    private MotorcycleService motorcycleService;

    @Autowired
    private YardService yardService;

    @GetMapping
    public String listMotorcycles(Model model) {
        model.addAttribute("motorcycles", motorcycleService.list(null, Pageable.unpaged()).getContent());
        return "motorcycle/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("motorcycle", new MotorcycleRequestDTO("", "", "", Year.now().getValue(), null));
        model.addAttribute("yards", yardService.list(null, Pageable.unpaged()).getContent());
        model.addAttribute("pageTitle", "Nova Motocicleta");
        return "motorcycle/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        var motorcycleDTO = motorcycleService.getById(id);
        var requestDTO = new MotorcycleRequestDTO(
                motorcycleDTO.model(),
                motorcycleDTO.plate(),
                motorcycleDTO.manufacturer(),
                motorcycleDTO.year(),
                motorcycleDTO.yardId()
        );
        model.addAttribute("motorcycle", requestDTO);
        model.addAttribute("motorcycleId", id);
        model.addAttribute("yards", yardService.list(null, Pageable.unpaged()).getContent());
        model.addAttribute("pageTitle", "Editar Motocicleta");
        return "motorcycle/form";
    }

    @PostMapping("/save")
    public String saveMotorcycle(@Valid @ModelAttribute("motorcycle") MotorcycleRequestDTO dto,
                                 @RequestParam(value = "id", required = false) UUID id,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("yards", yardService.list(null, Pageable.unpaged()).getContent());
            model.addAttribute("pageTitle", id == null ? "Nova Motocicleta" : "Editar Motocicleta");
            return "motorcycle/form";
        }

        try {
            if (id == null) {
                motorcycleService.create(dto);
                redirectAttributes.addFlashAttribute("message", "Motocicleta criada com sucesso!");
            } else {
                motorcycleService.update(id, dto);
                redirectAttributes.addFlashAttribute("message", "Motocicleta atualizada com sucesso!");
            }
        } catch (IllegalArgumentException e) {
            result.rejectValue("plate", "plate.duplicate", e.getMessage());
            model.addAttribute("yards", yardService.list(null, Pageable.unpaged()).getContent());
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

    // NOVOS MÉTODOS PARA MOVER A MOTOCICLETA
    @GetMapping("/move/{id}")
    public String showMoveForm(@PathVariable UUID id, Model model) {
        model.addAttribute("motorcycle", motorcycleService.getById(id));
        model.addAttribute("yards", yardService.list(null, Pageable.unpaged()).getContent());
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