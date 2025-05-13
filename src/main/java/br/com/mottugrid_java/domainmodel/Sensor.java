package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "sensor")
@Entity
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal batteryLevel;

    @Column(nullable = false)
    private LocalDateTime lastUpdate;

    @OneToOne
    @JoinColumn(name = "motorcycle_id")
    private Motorcycle motorcycle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sensor sensor)) return false;
        return Objects.equals(id, sensor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}