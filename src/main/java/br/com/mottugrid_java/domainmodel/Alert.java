package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import br.com.mottugrid_java.domainmodel.enums.AlertType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "alert")
@Entity
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AlertType type;

    @Column(nullable = false, length = 200)
    private String message;

    @Column(nullable = false)
    private LocalDateTime triggeredAt;

    @Column(nullable = false)
    private Boolean isResolved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorcycle_id")
    private Motorcycle motorcycle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alert alert)) return false;
        return Objects.equals(id, alert.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}