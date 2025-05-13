package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import br.com.mottugrid_java.domainmodel.enums.MotorcycleStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "motorcycle")
@Entity
public class Motorcycle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, unique = true, length = 10)
    private String licensePlate;

    @Column(nullable = false)
    private Integer engineCapacity;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotorcycleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yard_id")
    private Yard yard;

    @Embedded
    private Coordinates currentPosition;

    @OneToOne(mappedBy = "motorcycle", cascade = CascadeType.ALL)
    private Sensor sensor;

    @OneToMany(mappedBy = "motorcycle", cascade = CascadeType.ALL)
    private List<LocationEvent> locationHistory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Motorcycle motorcycle)) return false;
        return Objects.equals(id, motorcycle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}