package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {
    Optional<Organisation> findOrganisationByUsername(String username);

    Optional<Organisation> findOrganisationByNameIgnoreCase(String organisationName);

    boolean existsByUsername(String username);
}