package com.volunnear.repositories.infos;

import com.volunnear.entity.infos.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Optional<Organization> findOrganizationByUsername(String username);

    Optional<Organization> findOrganizationByNameIgnoreCase(String organisationName);

    boolean existsByUsername(String username);
}