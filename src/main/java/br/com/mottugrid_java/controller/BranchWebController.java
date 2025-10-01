package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.dto.BranchRequestDTO;
import br.com.mottugrid_java.dto.BranchResponseDTO;
import br.com.mottugrid_java.service.BranchService;
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
@RequestMapping("/branches")
public class BranchWebController {

    @Autowired
    private BranchService branchService;

    @GetMapping
    public String listBranches(Model model) {
        model.addAttribute("branches", branchService.list(null, Pageable.unpaged()).getContent());
        return "branch/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("branch", new BranchRequestDTO("", "", "", ""));
        model.addAttribute("pageTitle", "Nova Filial");
        return "branch/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        BranchResponseDTO dto = branchService.getById(id);
        BranchRequestDTO requestDTO = new BranchRequestDTO(dto.name(), dto.city(), dto.state(), dto.phone());
        model.addAttribute("branch", requestDTO);
        model.addAttribute("branchId", id);
        model.addAttribute("pageTitle", "Editar Filial");
        return "branch/form";
    }

    @PostMapping("/save")
    public String saveBranch(@Valid @ModelAttribute("branch") BranchRequestDTO dto,
                             @RequestParam(value = "id", required = false) UUID id,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", id == null ? "Nova Filial" : "Editar Filial");
            return "branch/form";
        }

        if (id == null) {
            branchService.create(dto);
            redirectAttributes.addFlashAttribute("message", "Filial criada com sucesso!");
        } else {
            branchService.update(id, dto);
            redirectAttributes.addFlashAttribute("message", "Filial atualizada com sucesso!");
        }
        return "redirect:/branches";
    }

    @GetMapping("/delete/{id}")
    public String deleteBranch(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            branchService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Filial removida com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Não foi possível remover. Verifique se a filial não possui pátios associados.");
        }
        return "redirect:/branches";
    }
}
