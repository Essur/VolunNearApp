package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.VolunteersSubscription;
import com.volunnear.entitiy.infos.VolunteersSubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VolunteersSubscriptionRepository extends JpaRepository<VolunteersSubscription, VolunteersSubscriptionId> {
    boolean existsByVolunteer_UsernameAndOrganization_Id(String username, Integer id);

    Optional<VolunteersSubscription> findByVolunteer_UsernameAndOrganization_Id(String username, Integer id);

    List<VolunteersSubscription> findAllByVolunteer_Username(String username);

    List<VolunteersSubscription> findAllByOrganization_Id(Integer id);

}