package com.volunnear.repository.infos;

import com.volunnear.entity.infos.OrganizationGroupLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationGroupLinkRepository extends JpaRepository<OrganizationGroupLink, Integer> {
    boolean existsByOrganization_Username(String username);

    Optional<OrganizationGroupLink> findOrganizationGroupLinkByOrganization_Id(Integer id);
}