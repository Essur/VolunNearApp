package com.volunnear.services;

import com.volunnear.dtos.requests.AddActivityLinkRequest;
import com.volunnear.dtos.requests.AddCommunityLinkRequest;
import com.volunnear.dtos.response.ActivityChatLinkResponseDTO;
import com.volunnear.entitiy.activities.Activities;
import com.volunnear.entitiy.infos.ActivityChatLink;
import com.volunnear.entitiy.infos.Organization;
import com.volunnear.entitiy.infos.OrganizationGroupLink;
import com.volunnear.exception.BadDataInRequestException;
import com.volunnear.exception.BadUserCredentialsException;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.repositories.infos.ActivityChatLinkRepository;
import com.volunnear.repositories.infos.OrganizationGroupLinkRepository;
import com.volunnear.services.activities.ActivityService;
import com.volunnear.services.users.OrganizationService;
import lombok.RequiredArgsConstructor;
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

    public Integer addChatLinkToActivity(AddActivityLinkRequest linkRequestDTO, Principal principal) {
        Optional<Activities> activity = activityService.findActivityByOrganizationAndIdOfActivity(principal, linkRequestDTO.getActivityId());
        if (activity.isEmpty() || !principal.getName().equals(activity.get().getOrganization().getUsername())) {
            throw new DataNotFoundException("Activity with id " + linkRequestDTO.getActivityId() + " not found");
        } else if (activityChatLinkRepository.existsByActivity_Id(linkRequestDTO.getActivityId())) {
            // TODO: write update link method
            throw new BadDataInRequestException("Link already exists");
        }
        ActivityChatLink activityChatLink = new ActivityChatLink();
        activityChatLink.setLink(linkRequestDTO.getLink());
        activityChatLink.setSocialNetwork(linkRequestDTO.getSocialNetwork());

        activityChatLink.setActivity(activity.get().getActivity());
        activityChatLink.setOrganization(activity.get().getOrganization());
        activityChatLinkRepository.save(activityChatLink);
        return activityChatLink.getId();
    }

    public String getChatLinkByActivityId(Integer idOfActivity) {
        Optional<ActivityChatLink> linkByActivityId = activityChatLinkRepository.findActivityChatLinkByActivity_Id(idOfActivity);
        if (linkByActivityId.isEmpty()) {
            throw new DataNotFoundException("Activity with id " + idOfActivity + " not founded");
        }
        ActivityChatLinkResponseDTO activityChatLink = new ActivityChatLinkResponseDTO();
        activityChatLink.setLink(linkByActivityId.get().getLink());
        activityChatLink.setActivityName(linkByActivityId.get().getActivity().getTitle());
        activityChatLink.setActivityId(linkByActivityId.get().getActivity().getId());
        activityChatLink.setSocialNetwork(linkByActivityId.get().getSocialNetwork());
        return activityChatLink.getLink();
    }

    public Integer addCommunityLink(AddCommunityLinkRequest communityLinkRequestDTO, Principal principal) {
        if (organizationGroupLinkRepository.existsByOrganization_Username(principal.getName())) {
            throw new BadDataInRequestException("Link already exists");
        } else if (!organizationService.isUserAreOrganization(principal.getName())) {
            throw new BadUserCredentialsException("You are not organization, try re-login");
        }
        Organization organization = organizationService.findOrganizationByUsername(principal.getName()).get();
        OrganizationGroupLink organizationGroupLink = new OrganizationGroupLink();

        organizationGroupLink.setOrganization(organization);
        organizationGroupLink.setLink(communityLinkRequestDTO.getLink());
        organizationGroupLink.setSocialNetwork(communityLinkRequestDTO.getSocialNetwork());
        organizationGroupLinkRepository.save(organizationGroupLink);
        return organizationGroupLink.getId();
    }

    public String getCommunityLinkByOrganizationId(Integer idOfOrganization) {
        Optional<OrganizationGroupLink> organizationGroupLink = organizationGroupLinkRepository.findOrganizationGroupLinkByOrganization_Id(idOfOrganization);
        if (organizationGroupLink.isEmpty()) {
            throw new DataNotFoundException("Organization with id " + idOfOrganization + " was not found");
        }
        OrganizationGroupLink organizationGroupLinkResponse = organizationGroupLink.get();
        return organizationGroupLinkResponse.getLink();
    }
}
