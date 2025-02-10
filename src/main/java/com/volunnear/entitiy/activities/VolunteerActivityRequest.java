package com.volunnear.entitiy.activities;

import com.volunnear.ActivityRequestStatus;
import com.volunnear.entitiy.infos.Volunteer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "volunteer_activity_request")
public class VolunteerActivityRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Enumerated(EnumType.STRING)
    private ActivityRequestStatus status = ActivityRequestStatus.PENDING; // Default

    private Instant requestDate = Instant.now();
    private Instant approvedDate;

}