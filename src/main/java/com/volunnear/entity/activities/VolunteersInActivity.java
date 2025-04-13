package com.volunnear.entity.activities;

import com.volunnear.entity.infos.Volunteer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "volunteers_in_activity")
public class VolunteersInActivity {
    @EmbeddedId
    private VolunteersInActivityId id;

    @MapsId("volunteerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @MapsId("activityId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "date_of_entry")
    private Instant dateOfEntry;

}