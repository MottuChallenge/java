package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "motorcycles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Motorcycle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, unique = true)
    private String plate;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yard_id", nullable = false)
    private Yard yard;
}
