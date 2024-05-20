package com.volunnear.services;

import com.volunnear.dtos.requests.AddActivityLinkRequestDTO;
import com.volunnear.dtos.requests.AddCommunityLinkRequestDTO;
import com.volunnear.dtos.response.ActivityChatLinkResponseDTO;
import com.volunnear.entitiy.activities.Activities;
import com.volunnear.entitiy.infos.ActivityChatLink;
import com.volunnear.entitiy.infos.Organisation;
import com.volunnear.entitiy.infos.OrganisationGroupLink;
import com.volunnear.repositories.infos.ActivityChatLinkRepository;
import com.volunnear.repositories.infos.OrganisationGroupLinkRepository;
import com.volunnear.services.activities.ActivityService;
import com.volunnear.services.users.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialMediaIntegrationService {
    private final ActivityService activityService;
    private final OrganisationService organisationService;
    private final ActivityChatLinkRepository activityChatLinkRepository;
    private final OrganisationGroupLinkRepository organisationGroupLinkRepository;

    public ResponseEntity<?> addChatLinkToActivity(AddActivityLinkRequestDTO linkRequestDTO, Principal principal) {
        Optional<Activities> activity = activityService.findActivityByOrganisationAndIdOfActivity(principal, linkRequestDTO.getActivityId());
        if (activity.isEmpty() || !principal.getName().equals(activity.get().getOrganisation().getUsername())) {
            return new ResponseEntity<>("Bad id of activity!", HttpStatus.BAD_REQUEST);
        } else if (activityChatLinkRepository.existsByActivity_Id(linkRequestDTO.getActivityId())) {
            return new ResponseEntity<>("Link already exists", HttpStatus.BAD_REQUEST);
        }
        ActivityChatLink activityChatLink = new ActivityChatLink();
        activityChatLink.setLink(linkRequestDTO.getLink());
        activityChatLink.setSocialNetwork(linkRequestDTO.getSocialNetwork());

        activityChatLink.setActivity(activity.get().getActivity());
        activityChatLink.setOrganisation(activity.get().getOrganisation());
        activityChatLinkRepository.save(activityChatLink);
        return new ResponseEntity<>("Successfully added link to activity with title " + activity.get().getActivity().getTitle(), HttpStatus.OK);
    }

    public ResponseEntity<String> getChatLinkByActivityId(Integer idOfActivity) {
        Optional<ActivityChatLink> linkByActivityId = activityChatLinkRepository.findActivityChatLinkByActivity_Id(idOfActivity);
        if (linkByActivityId.isEmpty()) {
            return new ResponseEntity<>("Activity with id " + idOfActivity + " not founded", HttpStatus.BAD_REQUEST);
        }
        ActivityChatLinkResponseDTO activityChatLink = new ActivityChatLinkResponseDTO();
        activityChatLink.setLink(linkByActivityId.get().getLink());
        activityChatLink.setActivityName(linkByActivityId.get().getActivity().getTitle());
        activityChatLink.setActivityId(linkByActivityId.get().getActivity().getId());
        activityChatLink.setSocialNetwork(linkByActivityId.get().getSocialNetwork());
        return new ResponseEntity<>("There`s link for chat of activity " + activityChatLink.getLink() + " social network: " + activityChatLink.getSocialNetwork(), HttpStatus.OK);
    }

    public ResponseEntity<?> addCommunityLink(AddCommunityLinkRequestDTO communityLinkRequestDTO, Principal principal) {
        if (organisationGroupLinkRepository.existsByOrganisation_Username(principal.getName())) {
            return new ResponseEntity<>("Link already exists", HttpStatus.BAD_REQUEST);
        } else if (!organisationService.isUserAreOrganisation(principal.getName())) {
            return new ResponseEntity<>("Bad id of organisation!", HttpStatus.BAD_REQUEST);
        }
        Organisation organisation = organisationService.findOrganisationByUsername(principal.getName()).get();
        OrganisationGroupLink organisationGroupLink = new OrganisationGroupLink();

        organisationGroupLink.setOrganisation(organisation);
        organisationGroupLink.setLink(communityLinkRequestDTO.getLink());
        organisationGroupLink.setSocialNetwork(communityLinkRequestDTO.getSocialNetwork());
        organisationGroupLinkRepository.save(organisationGroupLink);
        return new ResponseEntity<>("Successfully added community link to organisation " + organisation.getName(), HttpStatus.OK);
    }

    public ResponseEntity<String> getCommunityLinkByOrganisationId(Integer idOfOrganisation) {
        Optional<OrganisationGroupLink> organisationGroupLink = organisationGroupLinkRepository.findOrganisationGroupLinkByOrganisation_Id(idOfOrganisation);
        if (organisationGroupLink.isEmpty()) {
            return new ResponseEntity<>("Bad id of organisation!", HttpStatus.BAD_REQUEST);
        }
        OrganisationGroupLink organisationGroupLinkResponse = organisationGroupLink.get();
        return new ResponseEntity<>("There`s link for community of organisation " + organisationGroupLinkResponse.getLink() +
                " social network: " + organisationGroupLinkResponse.getSocialNetwork(), HttpStatus.OK);
    }
}
