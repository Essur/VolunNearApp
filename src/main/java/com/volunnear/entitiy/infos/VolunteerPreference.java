package com.volunnear.entitiy.infos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "volunteer_preferences")
public class VolunteerPreference {
    @EmbeddedId
    private VolunteerPreferenceId id;

    @MapsId("volunteerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @MapsId("preferenceId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "preference_id", nullable = false)
    private Preference preference;

}