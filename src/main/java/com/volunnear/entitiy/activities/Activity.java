package com.volunnear.entitiy.activities;

import com.volunnear.entitiy.infos.Preference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @Size(max = 225)
    @Column(name = "description", length = 225)
    private String description;

    @ManyToOne
    @JoinColumn(name = "kind_of_activity")
    private Preference kindOfActivity;

    @Column(name = "date_of_place")
    private Instant dateOfPlace;

}