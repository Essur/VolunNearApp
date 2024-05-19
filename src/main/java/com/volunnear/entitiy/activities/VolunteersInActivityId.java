package com.volunnear.entitiy.activities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class VolunteersInActivityId implements Serializable {
    private static final long serialVersionUID = 3119455508186986644L;
    @NotNull
    @Column(name = "volunteer_id", nullable = false)
    private Integer volunteerId;

    @NotNull
    @Column(name = "activity_id", nullable = false)
    private Integer activityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VolunteersInActivityId entity = (VolunteersInActivityId) o;
        return Objects.equals(this.activityId, entity.activityId) &&
                Objects.equals(this.volunteerId, entity.volunteerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, volunteerId);
    }

}