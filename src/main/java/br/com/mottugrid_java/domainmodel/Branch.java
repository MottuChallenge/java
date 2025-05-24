package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "branches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private List<Yard> yards = new ArrayList<>();


    public void addYard(Yard yard) {
        yards.add(yard);
        yard.setBranch(this);
    }


    public void removeYard(Yard yard) {
        yards.remove(yard);
        yard.setBranch(null);
    }
}
