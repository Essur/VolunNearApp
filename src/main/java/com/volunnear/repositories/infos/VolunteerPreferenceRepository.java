package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.Volunteer;
import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.entitiy.infos.VolunteerPreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerPreferenceRepository extends JpaRepository<VolunteerPreference, VolunteerPreferenceId> {
    List<VolunteerPreference> findAllByVolunteer_Username(String username);

    boolean deletePreferenceByIdAndVolunteer(VolunteerPreferenceId id, Volunteer volunteer);
}