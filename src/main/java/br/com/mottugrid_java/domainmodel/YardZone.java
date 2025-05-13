package br.com.mottugrid_java.domainmodel;

import br.com.mottugrid_java.domainmodel.enums.ZoneType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "yard_zone")
@Entity
public class YardZone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String zoneCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yard_id", nullable = false)
    private Yard yard;

    @Embedded
    private PolygonCoordinates area;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ZoneType type; // PARKING, MAINTENANCE, ENTRANCE, etc.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YardZone yardZone)) return false;
        return Objects.equals(id, yardZone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}