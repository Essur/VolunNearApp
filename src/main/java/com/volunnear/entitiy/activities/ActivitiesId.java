package com.volunnear.entitiy.activities;

import com.volunnear.entitiy.infos.Organization;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ActivitiesId implements Serializable {
    @Serial
    private static final long serialVersionUID = 3828002192702928374L;
    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organizationId;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ActivitiesId entity = (ActivitiesId) o;
        return Objects.equals(this.activityId, entity.activityId) &&
                Objects.equals(this.organizationId, entity.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, organizationId);
    }
}
