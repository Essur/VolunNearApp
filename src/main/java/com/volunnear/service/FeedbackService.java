package com.volunnear.service;

import com.volunnear.dto.requests.FeedbackRequest;
import com.volunnear.dto.response.FeedbackResponseDTO;
import com.volunnear.dto.response.OrganizationResponseDTO;
import com.volunnear.entity.infos.Feedback;
import com.volunnear.entity.infos.Organization;
import com.volunnear.entity.infos.Volunteer;
import com.volunnear.exception.BadDataInRequestException;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.repository.infos.FeedbackRepository;
import com.volunnear.service.user.OrganizationService;
import com.volunnear.service.user.VolunteerService;
import jakarta.transaction.Transactional;
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

    @Transactional
    public Integer postFeedbackAboutOrganization(FeedbackRequest feedbackRequest, Principal principal) {
        if (feedbackRequest.getRate() <= 0 || feedbackRequest.getRate() > 5) {
            throw new BadDataInRequestException("Bad value of rate, value should be between 1 and 5");
        }
        if (feedbackAboutOrganizationRepository.existsFeedbackByVolunteerFeedbackAuthor_User_Username(principal.getName())) {
            throw new BadDataInRequestException("Request for that user already exist, you can modify it!");
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

    @Transactional
    public FeedbackResponseDTO updateFeedbackInfoForCurrentOrganization(Integer idOfFeedback, FeedbackRequest feedbackRequest, Principal principal) {
        if (feedbackRequest.getRate() <= 0 || feedbackRequest.getRate() > 5) {
            throw new BadDataInRequestException("Bad value of rate, value should be between 1 and 5");
        }

        Optional<Feedback> feedbackById = feedbackAboutOrganizationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getVolunteerFeedbackAuthor().getUser().getUsername())) {
            throw new BadDataInRequestException("Feedback with id " + idOfFeedback + " was not found");
        }
        Feedback feedbackAboutOrganization = feedbackById.get();
        feedbackAboutOrganization.setDescription(feedbackRequest.getFeedbackDescription());
        feedbackAboutOrganization.setRate(feedbackRequest.getRate());
        feedbackAboutOrganizationRepository.save(feedbackAboutOrganization);

        return new FeedbackResponseDTO(feedbackAboutOrganization.getId() ,
                feedbackAboutOrganization.getRate(),
                feedbackAboutOrganization.getDescription(),
                feedbackAboutOrganization.getVolunteerFeedbackAuthor().getLastName() + " " + feedbackAboutOrganization.getVolunteerFeedbackAuthor().getFirstName(),
                principal.getName());
    }

    @Transactional
    public void deleteFeedbackAboutOrganization(Integer idOfFeedback, Principal principal) {
        Optional<Feedback> feedbackById = feedbackAboutOrganizationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getVolunteerFeedbackAuthor().getUser().getUsername())) {
            throw new DataNotFoundException("Invalid id of feedback");
        }
        feedbackAboutOrganizationRepository.deleteById(idOfFeedback);
    }

    public Map<OrganizationResponseDTO, List<FeedbackResponseDTO>> getAllFeedbacksAboutAllOrganizations() {
        List<Feedback> allFeedback = feedbackAboutOrganizationRepository.findAll();
        return getOrganizationResponseDTOMap(allFeedback);
    }

    public  List<FeedbackResponseDTO> getFeedbacksAboutCurrentOrganization(Integer id) {
        List<Feedback> feedbackAboutOrganizationList =
                feedbackAboutOrganizationRepository.findAllByTargetOrganization_Id(id);
        if (feedbackAboutOrganizationList.isEmpty()) {
            throw new DataNotFoundException("There is no feedback about that organization");
        }
        return getListOfFeedbacksDTO(feedbackAboutOrganizationList);
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
                feedbackAboutOrganization.getVolunteerFeedbackAuthor().getUser().getUsername()
        )).toList();
    }

}
