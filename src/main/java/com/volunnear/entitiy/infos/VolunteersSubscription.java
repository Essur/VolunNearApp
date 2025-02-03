package com.volunnear.entitiy.infos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "volunteers_subscriptions")
public class VolunteersSubscription {
    @EmbeddedId
    private VolunteersSubscriptionId id;

    @MapsId("volunteerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @MapsId("organizationId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

}