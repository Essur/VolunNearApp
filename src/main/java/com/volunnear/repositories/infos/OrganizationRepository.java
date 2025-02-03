package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Optional<Organization> findOrganizationByUsername(String username);

    Optional<Organization> findOrganizationByNameIgnoreCase(String organisationName);

    boolean existsByUsername(String username);
}