package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "yard")
@Entity
public class Yard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @OneToOne(mappedBy = "yard", cascade = CascadeType.ALL)
    private YardLayout layout;

    @OneToMany(mappedBy = "yard", cascade = CascadeType.ALL)
    private List<Motorcycle> motorcycles;

    @OneToMany(mappedBy = "yard", cascade = CascadeType.ALL)
    private List<YardZone> zones;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Yard yard)) return false;
        return Objects.equals(id, yard.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}