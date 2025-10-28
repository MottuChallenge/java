package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.dto.BranchRequestDTO;
import br.com.mottugrid_java.dto.BranchResponseDTO;
import br.com.mottugrid_java.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/branches")
public class BranchWebController {

    private final BranchService branchService;

    public BranchWebController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public String listBranches(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "name") Pageable pageable, // Recebe paginação
            Model model) {

        Page<Branch> entityPage = branchService.list(name, pageable);

        Page<BranchResponseDTO> responsePage = entityPage.map(branch ->
                new BranchResponseDTO(
                        branch.getId(),
                        branch.getName(),
                        branch.getCity(),
                        branch.getState(),
                        branch.getPhone()
                )
        );

        model.addAttribute("page", responsePage);
        model.addAttribute("name", name);

        return "branch/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Inicializa DTO vazio
        model.addAttribute("branch", new BranchRequestDTO(null, null, null, null));
        model.addAttribute("pageTitle", "Nova Filial");
        return "branch/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {

        Branch branch = branchService.getById(id);

        BranchRequestDTO requestDTO = new BranchRequestDTO(
                branch.getName(),
                branch.getCity(),
                branch.getState(),
                branch.getPhone()
        );

        model.addAttribute("branch", requestDTO);
        model.addAttribute("pageTitle", "Editar Filial");
        return "branch/form";
    }

    @PostMapping("/save") // Mapeamento corrigido
    public String saveBranch(@Valid @ModelAttribute("branch") BranchRequestDTO dto,
                             @RequestParam(value = "id", required = false) UUID id,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", id == null ? "Nova Filial" : "Editar Filial");
            return "branch/form";
        }

        Branch branchEntity = Branch.builder()
                .name(dto.name())
                .city(dto.city())
                .state(dto.state())
                .phone(dto.phone())
                .build();

        if (id == null) {
            branchService.create(branchEntity); // Service espera Entidade
            redirectAttributes.addFlashAttribute("message", "Filial criada com sucesso!");
        } else {
            branchService.update(id, branchEntity); // Service espera Entidade
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