package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import br.com.mottugrid_java.domainmodel.enums.LocationSource;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "location_event")
@Entity
public class LocationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private Coordinates coordinates;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LocationSource source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorcycle_id")
    private Motorcycle motorcycle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationEvent that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}