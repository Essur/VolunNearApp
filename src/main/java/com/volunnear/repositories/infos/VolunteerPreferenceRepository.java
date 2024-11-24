package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.entitiy.infos.VolunteerPreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VolunteerPreferenceRepository extends JpaRepository<VolunteerPreference, VolunteerPreferenceId> {
    List<VolunteerPreference> findAllByVolunteer_Username(String username);

    void deleteVolunteerPreferenceByPreference_IdAndVolunteer_Id(Integer preferenceId, Integer volunteerId);
    Optional<VolunteerPreference> findVolunteerPreferenceByPreference_IdAndVolunteer_Id(Integer preferenceId, Integer volunteerId);
}