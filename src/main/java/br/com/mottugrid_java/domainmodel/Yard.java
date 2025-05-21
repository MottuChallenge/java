package br.com.mottugrid_java.domainmodel;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "yards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Yard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    // Por enquanto comentado — vamos adicionar Branch e Address depois
    // @ManyToOne(optional = false)
    // private Branch branch;

    // @OneToOne(cascade = CascadeType.ALL)
    // private Address address;
}

