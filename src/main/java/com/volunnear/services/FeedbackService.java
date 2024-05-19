package com.volunnear.services;

import com.volunnear.dtos.requests.FeedbackRequest;
import com.volunnear.dtos.response.FeedbackResponseDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.infos.Feedback;
import com.volunnear.entitiy.infos.Organisation;
import com.volunnear.entitiy.infos.Volunteer;
import com.volunnear.repositories.infos.FeedbackRepository;
import com.volunnear.services.users.OrganisationService;
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
    private final OrganisationService organisationService;
    private final FeedbackRepository feedbackAboutOrganisationRepository;

    public ResponseEntity<String> postFeedbackAboutOrganisation(FeedbackRequest feedbackRequest, Principal principal) {
        if (feedbackRequest.getRate() < 0 || feedbackRequest.getRate() > 10) {
            return new ResponseEntity<>("Bad value of rate", HttpStatus.BAD_REQUEST);
        }

        Volunteer volunteer = volunteerService.getVolunteerInfo(principal).get();
        Optional<Organisation> organisationById = organisationService.findOrganisationById(feedbackRequest.getIdOfOrganisation());

        if (organisationById.isEmpty()) {
            return new ResponseEntity<>("No additional data about selected organisation", HttpStatus.BAD_REQUEST);
        }

        Feedback feedback = new Feedback();
        feedback.setTargetOrganisation(organisationById.get());
        feedback.setVolunteerFeedbackAuthor(volunteer);
        feedback.setRate(feedbackRequest.getRate());
        feedback.setDescription(feedbackRequest.getFeedbackDescription());
        feedbackAboutOrganisationRepository.save(feedback);
        return new ResponseEntity<>("Feedback successfully added", HttpStatus.OK);
    }

    public ResponseEntity<String> updateFeedbackInfoForCurrentOrganisation(Integer idOfFeedback, FeedbackRequest feedbackRequest, Principal principal) {
        Optional<Feedback> feedbackById = feedbackAboutOrganisationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getVolunteerFeedbackAuthor().getUsername())) {
            return new ResponseEntity<>("Invalid id of feedback", HttpStatus.BAD_REQUEST);
        }
        Feedback feedbackAboutOrganisation = feedbackById.get();
        feedbackAboutOrganisation.setDescription(feedbackRequest.getFeedbackDescription());
        feedbackAboutOrganisation.setRate(feedbackRequest.getRate());
        feedbackAboutOrganisationRepository.save(feedbackAboutOrganisation);
        return new ResponseEntity<>("Successfully updated feedback", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteFeedbackAboutOrganisation(Integer idOfFeedback, Principal principal) {
        Optional<Feedback> feedbackById = feedbackAboutOrganisationRepository.findById(idOfFeedback);
        if (feedbackById.isEmpty() || !principal.getName().equals(feedbackById.get().getVolunteerFeedbackAuthor().getUsername())) {
            return new ResponseEntity<>("Invalid id of feedback", HttpStatus.BAD_REQUEST);
        }
        feedbackAboutOrganisationRepository.deleteById(idOfFeedback);
        return new ResponseEntity<>("Successfully deleted your feedback", HttpStatus.OK);
    }

    public Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> getAllFeedbacksAboutAllOrganisations() {
        List<Feedback> allFeedback = feedbackAboutOrganisationRepository.findAll();
        return getOrganisationResponseDTOMap(allFeedback);
    }

    public ResponseEntity<?> getFeedbacksAboutCurrentOrganisation(Integer id) {
        List<Feedback> feedbackAboutOrganisationList =
                feedbackAboutOrganisationRepository.findAllByTargetOrganisation_Id(id);
        if (feedbackAboutOrganisationList.isEmpty()) {
            return new ResponseEntity<>("There is no feedback about that organisation", HttpStatus.OK);
        }
        Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> feedbackResult = getOrganisationResponseDTOMap(feedbackAboutOrganisationList);
        return new ResponseEntity<>(feedbackResult, HttpStatus.OK);
    }

    private Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> getOrganisationResponseDTOMap(List<Feedback> allFeedback) {
        Map<Organisation, List<Feedback>> feedbackMap =
                allFeedback.stream().collect(Collectors.groupingBy(Feedback::getTargetOrganisation));

        Map<OrganisationResponseDTO, List<FeedbackResponseDTO>> feedbackResult = new HashMap<>();
        for (Map.Entry<Organisation, List<Feedback>> organisationInfoListEntry : feedbackMap.entrySet()) {
            feedbackResult.put(organisationService.getOrganisationResponseDTO(organisationInfoListEntry.getKey()),
                    getListOfFeedbacksDTO(organisationInfoListEntry.getValue()));
        }
        return feedbackResult;
    }

    private List<FeedbackResponseDTO> getListOfFeedbacksDTO(List<Feedback> feedbackAboutOrganisationList) {
        return feedbackAboutOrganisationList.stream().map(feedbackAboutOrganisation -> new FeedbackResponseDTO(
                feedbackAboutOrganisation.getId(),
                feedbackAboutOrganisation.getRate(),
                feedbackAboutOrganisation.getDescription(),
                feedbackAboutOrganisation.getVolunteerFeedbackAuthor().getFirstName()
                        + " " + feedbackAboutOrganisation.getVolunteerFeedbackAuthor().getLastName(),
                feedbackAboutOrganisation.getVolunteerFeedbackAuthor().getUsername()

        )).toList();
    }

}
