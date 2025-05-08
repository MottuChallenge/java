package br.com.mottugrid_java.domainmodel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
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
    @Column(nullable = false, unique = true, length = 50)
    private String model;
    @Column(nullable = false, length = 50)
    private String brand;
    @Column(nullable = false)
    private int quantity = 0;
    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;
    @Column(name = "engine_capacity", nullable = false)
    private Integer engineCapacity;
    @Column(name = "license_plate", unique = true, length = 10)
    private String licensePlate;

    public void addMotorcycle(int quantity) {
        this.quantity += quantity;
    }
    public void removeMotorcycle(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
        } else {
            throw new IllegalArgumentException("Cannot remove more motorcycles than available");
        }
    }
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


