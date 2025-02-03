package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.OrganizationGroupLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationGroupLinkRepository extends JpaRepository<OrganizationGroupLink, Integer> {
    boolean existsByOrganization_Username(String username);

    Optional<OrganizationGroupLink> findOrganizationGroupLinkByOrganization_Id(Integer id);
}