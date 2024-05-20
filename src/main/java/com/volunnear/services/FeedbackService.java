package com.volunnear.services;

import com.volunnear.dtos.requests.FeedbackRequest;
import com.volunnear.dtos.response.FeedbackResponseDTO;
import com.volunnear.dtos.response.OrganizationResponseDTO;
import com.volunnear.entitiy.infos.Feedback;
import com.volunnear.entitiy.infos.Organization;
import com.volunnear.entitiy.infos.Volunteer;
import com.volunnear.repositories.infos.FeedbackRepository;
import com.volunnear.services.users.OrganizationService;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<String> postFeedbackAboutOrganization(FeedbackRequest feedbackRequest, Principal principal) {
        if (feedbackRequest.getRate() < 0 || feedbackRequest.getRate() > 10) {
            return new ResponseEntity<>("Bad value of rate", HttpStatus.BAD_REQUEST);
        }

        Volunteer volunteer = volunteerService.getVolunteerInfo(principal).get();
        Optional<Organization> organizationById = organizationService.findOrganizationById(feedbackRequest.getIdOfOrganization());

        if (organizationById.isEmpty()) {
            return new ResponseEntity<>("No additional data about selected organization", HttpStatus.BAD_REQUEST);
        }

        Feedback feedback = new Feedback();
        feedback.setTargetOrganization(organizationById.get());
        feedback.setVolunteerFeedbackAuthor(volunteer);
        feedback.setRate(feedbackRequest.getRate());
        feedback.setDescription(feedbackRequest.getFeedbackDescription());
        feedbackAboutOrganizationRepository.save(feedback);
        return new ResponseEntity<>("Feedback successfully added", HttpStatus.OK);
    }

    public ResponseEntity<String> updateFeedbackInfoForCurrentOrganization(Integer idOfFeedback, FeedbackRequest feedbackRequest, Principal principal) {
        Optional<Feedback> feedbackById = feedbackAboutOrganizationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getVolunteerFeedbackAuthor().getUsername())) {
            return new ResponseEntity<>("Invalid id of feedback", HttpStatus.BAD_REQUEST);
        }
        Feedback feedbackAboutOrganization = feedbackById.get();
        feedbackAboutOrganization.setDescription(feedbackRequest.getFeedbackDescription());
        feedbackAboutOrganization.setRate(feedbackRequest.getRate());
        feedbackAboutOrganizationRepository.save(feedbackAboutOrganization);
        return new ResponseEntity<>("Successfully updated feedback", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteFeedbackAboutOrganization(Integer idOfFeedback, Principal principal) {
        Optional<Feedback> feedbackById = feedbackAboutOrganizationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getVolunteerFeedbackAuthor().getUsername())) {
            return new ResponseEntity<>("Invalid id of feedback", HttpStatus.BAD_REQUEST);
        }
        feedbackAboutOrganizationRepository.deleteById(idOfFeedback);
        return new ResponseEntity<>("Successfully deleted your feedback", HttpStatus.OK);
    }

    public Map<OrganizationResponseDTO, List<FeedbackResponseDTO>> getAllFeedbacksAboutAllOrganizations() {
        List<Feedback> allFeedback = feedbackAboutOrganizationRepository.findAll();
        return getOrganizationResponseDTOMap(allFeedback);
    }

    public ResponseEntity<?> getFeedbacksAboutCurrentOrganization(Integer id) {
        List<Feedback> feedbackAboutOrganizationList =
                feedbackAboutOrganizationRepository.findAllByTargetOrganization_Id(id);
        if (feedbackAboutOrganizationList.isEmpty()) {
            return new ResponseEntity<>("There is no feedback about that organization", HttpStatus.OK);
        }
        Map<OrganizationResponseDTO, List<FeedbackResponseDTO>> feedbackResult = getOrganizationResponseDTOMap(feedbackAboutOrganizationList);
        return new ResponseEntity<>(feedbackResult, HttpStatus.OK);
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
