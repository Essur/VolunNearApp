package com.volunnear.entitiy.infos;

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
public class VolunteerPreferenceId implements Serializable {
    private static final long serialVersionUID = -3391418963390221207L;
    @NotNull
    @Column(name = "volunteer_id", nullable = false)
    private Integer volunteerId;

    @NotNull
    @Column(name = "preference_id", nullable = false)
    private Integer preferenceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VolunteerPreferenceId entity = (VolunteerPreferenceId) o;
        return Objects.equals(this.preferenceId, entity.preferenceId) &&
                Objects.equals(this.volunteerId, entity.volunteerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preferenceId, volunteerId);
    }

}