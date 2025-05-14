package com.volunnear.entity.infos;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.locationtech.jts.geom.Point;

import java.util.Objects;

@Entity
@Table(name = "volunteer_preference")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class VolunteerPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "volunteer_id", referencedColumnName = "volunteer_id", unique = true)
    private Volunteer volunteer;

    @Column(name = "location", columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point location;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        VolunteerPreference that = (VolunteerPreference) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}