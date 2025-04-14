package com.volunnear.entity.activities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@Table(name = "activity")
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id", nullable = false)
    private Integer id;

    @Size(max = 45)
    @Column(name = "city", length = 45)
    private String city;

    @Size(max = 45)
    @Column(name = "country", length = 45)
    private String country;

    @Size(max = 45)
    @Column(name = "title", length = 45)
    private String title;

    @Lob
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "kind_of_activity")
    private String kindOfActivity;

    @Column(name = "date_of_place")
    private Instant dateOfPlace;

    @Column(name = "location", columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point location;
}