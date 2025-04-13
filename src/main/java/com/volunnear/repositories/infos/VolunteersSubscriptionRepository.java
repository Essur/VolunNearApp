package com.volunnear.repositories.infos;

import com.volunnear.entity.infos.VolunteersSubscription;
import com.volunnear.entity.infos.VolunteersSubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteersSubscriptionRepository extends JpaRepository<VolunteersSubscription, VolunteersSubscriptionId> {
    boolean existsByVolunteer_User_UsernameAndOrganization_Id(String username, Integer id);

    Optional<VolunteersSubscription> findByVolunteer_User_UsernameAndOrganization_Id(String username, Integer id);

    List<VolunteersSubscription> findAllByVolunteer_User_Username(String username);

    List<VolunteersSubscription> findAllByOrganization_Id(Integer id);

}