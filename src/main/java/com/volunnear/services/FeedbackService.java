package com.volunnear.services;

import com.volunnear.dtos.requests.FeedbackRequest;
import com.volunnear.dtos.response.FeedbackResponseDTO;
import com.volunnear.dtos.response.OrganizationResponseDTO;
import com.volunnear.entitiy.infos.Feedback;
import com.volunnear.entitiy.infos.Organization;
import com.volunnear.entitiy.infos.Volunteer;
import com.volunnear.exception.BadDataInRequestException;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.repositories.infos.FeedbackRepository;
import com.volunnear.services.users.OrganizationService;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final VolunteerService volunteerService;
    private final OrganizationService organizationService;
    private final FeedbackRepository feedbackAboutOrganizationRepository;

    public Integer postFeedbackAboutOrganization(FeedbackRequest feedbackRequest, Principal principal) {
        if (feedbackRequest.getRate() < 0 || feedbackRequest.getRate() > 10) {
            throw new BadDataInRequestException("Bad value of rate, value should be between 0 and 10");
        }

        Volunteer volunteer = volunteerService.getVolunteerInfo(principal).get();
        Optional<Organization> organizationById = organizationService.findOrganizationById(feedbackRequest.getIdOfOrganization());

        if (organizationById.isEmpty()) {
            throw new DataNotFoundException("There is no organization with id " + feedbackRequest.getIdOfOrganization());
        }

        Feedback feedback = new Feedback();
        feedback.setTargetOrganization(organizationById.get());
        feedback.setVolunteerFeedbackAuthor(volunteer);
        feedback.setRate(feedbackRequest.getRate());
        feedback.setDescription(feedbackRequest.getFeedbackDescription());
        feedbackAboutOrganizationRepository.save(feedback);
        return feedback.getId();
    }

    public FeedbackResponseDTO updateFeedbackInfoForCurrentOrganization(Integer idOfFeedback, FeedbackRequest feedbackRequest, Principal principal) {
        Optional<Feedback> feedbackById = feedbackAboutOrganizationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getVolunteerFeedbackAuthor().getUsername())) {
            throw new BadDataInRequestException("Feedback with id " + idOfFeedback + " was not found");
        }
        Feedback feedbackAboutOrganization = feedbackById.get();
        feedbackAboutOrganization.setDescription(feedbackRequest.getFeedbackDescription());
        feedbackAboutOrganization.setRate(feedbackRequest.getRate());
        feedbackAboutOrganizationRepository.save(feedbackAboutOrganization);

        return new FeedbackResponseDTO(feedbackAboutOrganization.getId() ,
                feedbackAboutOrganization.getRate(),
                feedbackAboutOrganization.getDescription(),
                feedbackAboutOrganization.getVolunteerFeedbackAuthor().getLastName() + feedbackAboutOrganization.getVolunteerFeedbackAuthor().getFirstName(),
                principal.getName());
    }

    public void deleteFeedbackAboutOrganization(Integer idOfFeedback, Principal principal) {
        Optional<Feedback> feedbackById = feedbackAboutOrganizationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getVolunteerFeedbackAuthor().getUsername())) {
            throw new DataNotFoundException("Invalid id of feedback");
        }
        feedbackAboutOrganizationRepository.deleteById(idOfFeedback);
    }

    public Map<OrganizationResponseDTO, List<FeedbackResponseDTO>> getAllFeedbacksAboutAllOrganizations() {
        List<Feedback> allFeedback = feedbackAboutOrganizationRepository.findAll();
        return getOrganizationResponseDTOMap(allFeedback);
    }

    public  Map<OrganizationResponseDTO, List<FeedbackResponseDTO>> getFeedbacksAboutCurrentOrganization(Integer id) {
        List<Feedback> feedbackAboutOrganizationList =
                feedbackAboutOrganizationRepository.findAllByTargetOrganization_Id(id);
        if (feedbackAboutOrganizationList.isEmpty()) {
            throw new DataNotFoundException("There is no feedback about that organization");
        }
        return getOrganizationResponseDTOMap(feedbackAboutOrganizationList);
    }

    private Map<OrganizationResponseDTO, List<FeedbackResponseDTO>> getOrganizationResponseDTOMap(List<Feedback> allFeedback) {
        Map<Organization, List<Feedback>> feedbackMap =
                allFeedback.stream().collect(Collectors.groupingBy(Feedback::getTargetOrganization));

        Map<OrganizationResponseDTO, List<FeedbackResponseDTO>> feedbackResult = new HashMap<>();
        for (Map.Entry<Organization, List<Feedback>> organizationInfoListEntry : feedbackMap.entrySet()) {
            feedbackResult.put(organizationService.getOrganizationResponseDTO(organizationInfoListEntry.getKey()),
                    getListOfFeedbacksDTO(organizationInfoListEntry.getValue()));
        }
        return feedbackResult;
    }

    private List<FeedbackResponseDTO> getListOfFeedbacksDTO(List<Feedback> feedbackAboutOrganizationList) {
        return feedbackAboutOrganizationList.stream().map(feedbackAboutOrganization -> new FeedbackResponseDTO(
                feedbackAboutOrganization.getId(),
                feedbackAboutOrganization.getRate(),
                feedbackAboutOrganization.getDescription(),
                feedbackAboutOrganization.getVolunteerFeedbackAuthor().getFirstName()
                        + " " + feedbackAboutOrganization.getVolunteerFeedbackAuthor().getLastName(),
                feedbackAboutOrganization.getVolunteerFeedbackAuthor().getUsername()

        )).toList();
    }

}
