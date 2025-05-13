package br.com.mottugrid_java.domainmodel;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PolygonCoordinates {
    @ElementCollection
    @CollectionTable(name = "zone_coordinates",
            joinColumns = @JoinColumn(name = "zone_id"))
    private List<CoordinatePoint> points;
}