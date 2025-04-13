package com.volunnear.services.notifications;

import com.volunnear.dtos.response.OrganizationResponseDTO;
import com.volunnear.entity.infos.Organization;
import com.volunnear.entity.infos.Volunteer;
import com.volunnear.entity.infos.VolunteersSubscription;
import com.volunnear.entity.infos.VolunteersSubscriptionId;
import com.volunnear.exception.BadDataInRequestException;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.exception.UserAlreadyExistsException;
import com.volunnear.repositories.infos.VolunteersSubscriptionRepository;
import com.volunnear.services.users.OrganizationService;
import com.volunnear.services.users.VolunteerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {
    private final VolunteerService volunteerService;
    private final OrganizationService organizationService;
    private final VolunteersSubscriptionRepository subscriptionRepository;


    @Transactional
    public void subscribeToNotificationByIdOfOrganization(Integer idOfOrganization, Principal principal) {
        Optional<Organization> organizationById = organizationService.findOrganizationById(idOfOrganization);
        if (organizationById.isEmpty()) {
            throw new BadDataInRequestException("Organization with id " + idOfOrganization + " not found");
        }
        if (subscriptionRepository.existsByVolunteer_User_UsernameAndOrganization_Id(principal.getName(), idOfOrganization)) {
            throw new UserAlreadyExistsException("Fail, you are already subscribed");
        }
        Optional<Volunteer> volunteerInfo = volunteerService.getVolunteerInfo(principal);

        VolunteersSubscription subscription = new VolunteersSubscription();
        subscription.setId(new VolunteersSubscriptionId(volunteerInfo.get().getId(), organizationById.get().getId()));
        subscription.setOrganization(organizationById.get());
        subscription.setVolunteer(volunteerInfo.get());
        subscriptionRepository.save(subscription);
    }

    public void unsubscribeFromNotificationOfOrganization(Integer idOfOrganization, Principal principal) {
        Optional<VolunteersSubscription> subscriptionById = subscriptionRepository.
                findByVolunteer_User_UsernameAndOrganization_Id(principal.getName(), idOfOrganization);
        if (subscriptionById.isEmpty() || !principal.getName().equals(subscriptionById.get().getVolunteer().getUser().getUsername())) {
            throw new BadDataInRequestException("You are not subscribed to organization with id " + idOfOrganization);
        }
        subscriptionRepository.delete(subscriptionById.get());
    }

    public List<OrganizationResponseDTO> getAllSubscriptionsOfVolunteer(Principal principal) {
        List<VolunteersSubscription> allByUserVolunteerUsername = subscriptionRepository.findAllByVolunteer_User_Username(principal.getName());
        if (allByUserVolunteerUsername.isEmpty()) {
            throw new DataNotFoundException("List of subscriptions is empty!");
        }

        return allByUserVolunteerUsername.stream().map(
                subscription -> organizationService
                        .getResponseDTOForSubscriptions(subscription.getOrganization())).collect(Collectors.toList());
    }

    public void getNotificationSubscriptionStatus(Integer organizationId, Principal principal) {
        if (!subscriptionRepository.existsByVolunteer_User_UsernameAndOrganization_Id(principal.getName(), organizationId)) {
            throw new DataNotFoundException("Fail, you are not subscribed");
        }
    }
}
