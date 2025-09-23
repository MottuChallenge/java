package br.com.mottugrid_java.controller;


import br.com.mottugrid_java.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/branches")
public class BranchWebController {

    private final BranchService branchService;

    public BranchWebController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("branches", branchService.findAllEntities());
        return "branch/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("branch", new Branch());
        return "branch/form";
    }

    @PostMapping("/save")
    public String save(@Valid Branch branch, BindingResult result) {
        if (result.hasErrors()) {
            return "branch/form";
        }
        branchService.saveEntity(branch);
        return "redirect:/branches";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        Branch branch = branchService.findEntityById(id)
                .orElseThrow(() -> new IllegalArgumentException("Filial não encontrada"));
        model.addAttribute("branch", branch);
        return "branch/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        branchService.deleteEntity(id);
        return "redirect:/branches";
    }
}
