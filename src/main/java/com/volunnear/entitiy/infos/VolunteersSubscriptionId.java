package com.volunnear.entitiy.infos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VolunteersSubscriptionId implements Serializable {
    private static final long serialVersionUID = -3210833227201288785L;
    @NotNull
    @Column(name = "volunteer_id", nullable = false)
    private Integer volunteerId;

    @NotNull
    @Column(name = "organisation_id", nullable = false)
    private Integer organisationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VolunteersSubscriptionId entity = (VolunteersSubscriptionId) o;
        return Objects.equals(this.organisationId, entity.organisationId) &&
                Objects.equals(this.volunteerId, entity.volunteerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organisationId, volunteerId);
    }

}