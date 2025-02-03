package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<Preference, Integer> {
    Optional<Preference> findPreferenceByNameIgnoreCase(String name);

    List<Preference> findAllByNameIgnoreCaseIn(List<String> preferences);

    boolean existsPreferenceByNameIgnoreCase(String preference);

    Optional<Preference> findPreferenceById(Integer id);
}