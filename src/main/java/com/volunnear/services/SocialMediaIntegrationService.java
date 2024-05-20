package com.volunnear.services;

import com.volunnear.dtos.requests.AddActivityLinkRequestDTO;
import com.volunnear.dtos.requests.AddCommunityLinkRequestDTO;
import com.volunnear.dtos.response.ActivityChatLinkResponseDTO;
import com.volunnear.entitiy.activities.Activities;
import com.volunnear.entitiy.infos.ActivityChatLink;
import com.volunnear.entitiy.infos.Organization;
import com.volunnear.entitiy.infos.OrganizationGroupLink;
import com.volunnear.repositories.infos.ActivityChatLinkRepository;
import com.volunnear.repositories.infos.OrganizationGroupLinkRepository;
import com.volunnear.services.activities.ActivityService;
import com.volunnear.services.users.OrganizationService;
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
    private final OrganizationService organizationService;
    private final ActivityChatLinkRepository activityChatLinkRepository;
    private final OrganizationGroupLinkRepository organizationGroupLinkRepository;

    public ResponseEntity<?> addChatLinkToActivity(AddActivityLinkRequestDTO linkRequestDTO, Principal principal) {
        Optional<Activities> activity = activityService.findActivityByOrganizationAndIdOfActivity(principal, linkRequestDTO.getActivityId());
        if (activity.isEmpty() || !principal.getName().equals(activity.get().getOrganization().getUsername())) {
            return new ResponseEntity<>("Bad id of activity!", HttpStatus.BAD_REQUEST);
        } else if (activityChatLinkRepository.existsByActivity_Id(linkRequestDTO.getActivityId())) {
            return new ResponseEntity<>("Link already exists", HttpStatus.BAD_REQUEST);
        }
        ActivityChatLink activityChatLink = new ActivityChatLink();
        activityChatLink.setLink(linkRequestDTO.getLink());
        activityChatLink.setSocialNetwork(linkRequestDTO.getSocialNetwork());

        activityChatLink.setActivity(activity.get().getActivity());
        activityChatLink.setOrganization(activity.get().getOrganization());
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
        if (organizationGroupLinkRepository.existsByOrganization_Username(principal.getName())) {
            return new ResponseEntity<>("Link already exists", HttpStatus.BAD_REQUEST);
        } else if (!organizationService.isUserAreOrganization(principal.getName())) {
            return new ResponseEntity<>("Bad id of organization!", HttpStatus.BAD_REQUEST);
        }
        Organization organization = organizationService.findOrganizationByUsername(principal.getName()).get();
        OrganizationGroupLink organizationGroupLink = new OrganizationGroupLink();

        organizationGroupLink.setOrganization(organization);
        organizationGroupLink.setLink(communityLinkRequestDTO.getLink());
        organizationGroupLink.setSocialNetwork(communityLinkRequestDTO.getSocialNetwork());
        organizationGroupLinkRepository.save(organizationGroupLink);
        return new ResponseEntity<>("Successfully added community link to organization " + organization.getName(), HttpStatus.OK);
    }

    public ResponseEntity<String> getCommunityLinkByOrganizationId(Integer idOfOrganization) {
        Optional<OrganizationGroupLink> organizationGroupLink = organizationGroupLinkRepository.findOrganizationGroupLinkByOrganization_Id(idOfOrganization);
        if (organizationGroupLink.isEmpty()) {
            return new ResponseEntity<>("Bad id of organization!", HttpStatus.BAD_REQUEST);
        }
        OrganizationGroupLink organizationGroupLinkResponse = organizationGroupLink.get();
        return new ResponseEntity<>("There`s link for community of organization " + organizationGroupLinkResponse.getLink() +
                " social network: " + organizationGroupLinkResponse.getSocialNetwork(), HttpStatus.OK);
    }
}
