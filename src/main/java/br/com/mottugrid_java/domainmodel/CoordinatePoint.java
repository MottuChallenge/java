package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CoordinatePoint {
    private Double x;
    private Double y;
}