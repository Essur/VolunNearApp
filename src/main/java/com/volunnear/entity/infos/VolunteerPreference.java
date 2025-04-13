package com.volunnear.entity.infos;

import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "volunteer_preference")
@Data
public class VolunteerPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "username", referencedColumnName = "username", unique = true)
    private Volunteer volunteer;

    @Column(name = "location", columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point location;
}