package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "yards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Yard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    public void setBranch(Branch branch) {
        if (this.branch != null && this.branch != branch) {
            this.branch.getYards().remove(this);
        }
        this.branch = branch;

        if (branch != null && !branch.getYards().contains(this)) {
            branch.getYards().add(this);
        }
    }
}