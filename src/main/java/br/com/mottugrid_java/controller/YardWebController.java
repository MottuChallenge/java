package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.YardRequestDTO;
import br.com.mottugrid_java.service.BranchService;
import br.com.mottugrid_java.service.YardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/yards")
public class YardWebController {

    @Autowired
    private YardService yardService;

    @Autowired
    private BranchService branchService;

    @GetMapping
    public String listYards(Model model) {
        model.addAttribute("yards", yardService.list(null, Pageable.unpaged()).getContent());
        return "yard/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("yard", new YardRequestDTO("", null));
        model.addAttribute("branches", branchService.list(null, Pageable.unpaged()).getContent());
        model.addAttribute("pageTitle", "Novo Pátio");
        return "yard/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        var yardDTO = yardService.getById(id);
        var yardRequestDTO = new YardRequestDTO(yardDTO.name(), yardDTO.branchId());
        model.addAttribute("yard", yardRequestDTO);
        model.addAttribute("yardId", id);
        model.addAttribute("branches", branchService.list(null, Pageable.unpaged()).getContent());
        model.addAttribute("pageTitle", "Editar Pátio");
        return "yard/form";
    }

    @PostMapping("/save")
    public String saveYard(@Valid @ModelAttribute("yard") YardRequestDTO dto,
                           @RequestParam(value = "id", required = false) UUID id,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("branches", branchService.list(null, Pageable.unpaged()).getContent());
            model.addAttribute("pageTitle", id == null ? "Novo Pátio" : "Editar Pátio");
            return "yard/form";
        }

        if (id == null) {
            yardService.create(dto);
            redirectAttributes.addFlashAttribute("message", "Pátio criado com sucesso!");
        } else {
            yardService.update(id, dto);
            redirectAttributes.addFlashAttribute("message", "Pátio atualizado com sucesso!");
        }

        return "redirect:/yards";
    }

    @GetMapping("/delete/{id}")
    public String deleteYard(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            yardService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Pátio removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Não foi possível remover o pátio. Verifique se ele não possui motos associadas.");
        }
        return "redirect:/yards";
    }
}

