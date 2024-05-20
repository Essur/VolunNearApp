package com.volunnear.services.notifications;

import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.infos.Organisation;
import com.volunnear.entitiy.infos.Volunteer;
import com.volunnear.entitiy.infos.VolunteersSubscription;
import com.volunnear.entitiy.infos.VolunteersSubscriptionId;
import com.volunnear.repositories.infos.VolunteersSubscriptionRepository;
import com.volunnear.services.users.OrganisationService;
import com.volunnear.services.users.VolunteerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {
    private final VolunteerService volunteerService;
    private final OrganisationService organisationService;
    private final VolunteersSubscriptionRepository subscriptionRepository;


    @Transactional
    public ResponseEntity<String> subscribeToNotificationByIdOfOrganisation(Integer idOfOrganisation, Principal principal) {
        Optional<Organisation> organisationById = organisationService.findOrganisationById(idOfOrganisation);
        if (organisationById.isEmpty()) {
            return new ResponseEntity<>("Bad id of organisation", HttpStatus.BAD_REQUEST);
        }
        if (subscriptionRepository.existsByVolunteer_UsernameAndOrganisation_Id(principal.getName(), idOfOrganisation)) {
            return new ResponseEntity<>("Fail, you are already subscribed", HttpStatus.BAD_REQUEST);
        }
        Optional<Volunteer> volunteerInfo = volunteerService.getVolunteerInfo(principal);
        if (!volunteerService.isUserAreVolunteer(volunteerInfo.get())) {
            return new ResponseEntity<>("You are not volunteer", HttpStatus.BAD_REQUEST);
        }
        VolunteersSubscription subscription = new VolunteersSubscription();
        subscription.setId(new VolunteersSubscriptionId(volunteerInfo.get().getId(), organisationById.get().getId()));
        subscription.setOrganisation(organisationById.get());
        subscription.setVolunteer(volunteerInfo.get());
        subscriptionRepository.save(subscription);
        return new ResponseEntity<>("Successfully subscribed to notifications", HttpStatus.OK);
    }

    public ResponseEntity<String> unsubscribeFromNotificationOfOrganisation(Integer idOfOrganisation, Principal principal) {
        Optional<VolunteersSubscription> subscriptionById = subscriptionRepository.
                findByVolunteer_UsernameAndOrganisation_Id(principal.getName(), idOfOrganisation);
        if (subscriptionById.isEmpty() || !principal.getName().equals(subscriptionById.get().getVolunteer().getUsername())) {
            return new ResponseEntity<>("Bad id of subscription", HttpStatus.BAD_REQUEST);
        }
        subscriptionRepository.delete(subscriptionById.get());
        return new ResponseEntity<>("Successfully unsubscribed from notifications", HttpStatus.OK);
    }

    public ResponseEntity<?> getAllSubscriptionsOfVolunteer(Principal principal) {
        List<VolunteersSubscription> allByUserVolunteerUsername = subscriptionRepository.findAllByVolunteer_Username(principal.getName());
        if (allByUserVolunteerUsername.isEmpty()) {
            return new ResponseEntity<>("List of subscriptions is empty!", HttpStatus.BAD_REQUEST);
        }
        List<OrganisationResponseDTO> organisations = allByUserVolunteerUsername.stream().map(
                subscription -> organisationService.getResponseDTOForSubscriptions(subscription.getOrganisation())).collect(Collectors.toList());

        return new ResponseEntity<>(organisations, HttpStatus.OK);
    }

}
