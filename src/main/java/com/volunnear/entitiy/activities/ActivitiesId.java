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
public class ActivitiesId implements Serializable {
    private static final long serialVersionUID = 3828002192702928374L;
    @NotNull
    @Column(name = "organisation_id", nullable = false)
    private Integer organisationId;

    @NotNull
    @Column(name = "activity_id", nullable = false)
    private Integer activityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ActivitiesId entity = (ActivitiesId) o;
        return Objects.equals(this.activityId, entity.activityId) &&
                Objects.equals(this.organisationId, entity.organisationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, organisationId);
    }

}