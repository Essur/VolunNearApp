package com.volunnear.entity.activity;

import com.volunnear.entity.profile.OrganizationProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "schedule")
    private String schedule;

    @Column(name = "title")
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Lob
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "location", columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point location;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private OrganizationProfile organizationProfile;
}