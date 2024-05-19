package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.OrganisationGroupLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganisationGroupLinkRepository extends JpaRepository<OrganisationGroupLink, Integer> {
    boolean existsByOrganisation_Username(String username);

    Optional<OrganisationGroupLink> findOrganisationGroupLinkByOrganisation_Id(Integer id);
}