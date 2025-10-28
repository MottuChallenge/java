package br.com.mottugrid_java.controller;

import br.com.mottugrid_java.domainmodel.Branch;
import br.com.mottugrid_java.domainmodel.Yard;
import br.com.mottugrid_java.dto.YardRequestDTO;
import br.com.mottugrid_java.dto.YardResponseDTO;
import br.com.mottugrid_java.dto.BranchResponseDTO;
import br.com.mottugrid_java.service.BranchService;
import br.com.mottugrid_java.service.YardService;
import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException; // <-- NOVO: Correção do erro de compilação
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
@RequestMapping("/yards")
public class YardWebController {

    private final YardService yardService;
    private final BranchService branchService;

    // Injeção via Construtor (Melhor Prática)
    public YardWebController(YardService yardService, BranchService branchService) {
        this.yardService = yardService;
        this.branchService = branchService;
    }

    @GetMapping
    public String listYards(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            Model model) {

        // Service retorna Page<Yard> (Entidade)
        Page<Yard> entityPage = yardService.list(name, pageable);

        // CONVERSÃO INLINE: Page<Yard> -> Page<YardResponseDTO>
        Page<YardResponseDTO> responsePage = entityPage.map(yard -> new YardResponseDTO(
                yard.getId(),
                yard.getName(),
                yard.getBranch() != null ? yard.getBranch().getId() : null
        ));

        model.addAttribute("page", responsePage);
        model.addAttribute("name", name);

        return "yard/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("yard", new YardRequestDTO(null, null));

        // Carrega Branches (Service retorna Page<Branch>)
        Page<Branch> branchPage = branchService.list(null, Pageable.unpaged());

        // CONVERSÃO INLINE: Page<Branch> -> Page<BranchResponseDTO>
        Page<BranchResponseDTO> branchDTOPage = branchPage.map(branch -> new BranchResponseDTO(
                branch.getId(), branch.getName(), branch.getCity(), branch.getState(), branch.getPhone()
        ));

        model.addAttribute("branches", branchDTOPage.getContent());
        model.addAttribute("pageTitle", "Novo Pátio");
        return "yard/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        // Service retorna Entidade
        Yard yard = yardService.getById(id);

        // CONVERSÃO INLINE: Entity -> DTO de Request
        var yardRequestDTO = new YardRequestDTO(yard.getName(), yard.getBranch().getId());

        model.addAttribute("yard", yardRequestDTO);
        model.addAttribute("yardId", id);

        // Carrega Branches (conversão inline)
        Page<Branch> branchPage = branchService.list(null, Pageable.unpaged());
        Page<BranchResponseDTO> branchDTOPage = branchPage.map(branch -> new BranchResponseDTO(
                branch.getId(), branch.getName(), branch.getCity(), branch.getState(), branch.getPhone()
        ));
        model.addAttribute("branches", branchDTOPage.getContent());

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
            // Recarrega Branches em caso de erro
            Page<Branch> branchPage = branchService.list(null, Pageable.unpaged());
            Page<BranchResponseDTO> branchDTOPage = branchPage.map(branch -> new BranchResponseDTO(
                    branch.getId(), branch.getName(), branch.getCity(), branch.getState(), branch.getPhone()
            ));
            model.addAttribute("branches", branchDTOPage.getContent());
            model.addAttribute("pageTitle", id == null ? "Novo Pátio" : "Editar Pátio");
            return "yard/form";
        }

        // CONVERSÃO INLINE: DTO -> Entidade
        Yard yardEntity = Yard.builder()
                .name(dto.name())
                .branch(Branch.builder().id(dto.branchId()).build())
                .build();

        try {
            if (id == null) {
                yardService.create(yardEntity);
                redirectAttributes.addFlashAttribute("message", "Pátio criado com sucesso!");
            } else {
                yardService.update(id, yardEntity);
                redirectAttributes.addFlashAttribute("message", "Pátio atualizado com sucesso!");
            }
        } catch (EntityNotFoundException e) {
            // O uso de e.getMessage() aqui foi o motivo do segundo erro
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar: " + e.getMessage());
            return "redirect:/yards";
        }

        return "redirect:/yards";
    }

    @GetMapping("/delete/{id}")
    public String deleteYard(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            yardService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Pátio removido com sucesso!");
        } catch (Exception e) { // Catch genérico para remover dependência de EntityNotFoundException se não for necessário exibir a mensagem
            redirectAttributes.addFlashAttribute("error", "Não foi possível remover o pátio. Verifique se ele não possui motos associadas.");
        }
        return "redirect:/yards";
    }
}