package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "yard_layout")
@Entity
public class YardLayout {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Lob
    @Column(nullable = false)
    private String layoutData;

    @Column(length = 255)
    private String imageReference;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "yard_id")
    private Yard yard;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YardLayout yardLayout)) return false;
        return Objects.equals(id, yardLayout.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}