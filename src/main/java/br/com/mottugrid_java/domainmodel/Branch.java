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
@Table(name = "branch")
@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String country;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<Yard> yards;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Branch branch)) return false;
        return Objects.equals(id, branch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}