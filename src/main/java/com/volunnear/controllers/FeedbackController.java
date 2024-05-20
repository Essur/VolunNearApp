package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.FeedbackRequest;
import com.volunnear.dtos.response.FeedbackResponseDTO;
import com.volunnear.dtos.response.OrganizationResponseDTO;
import com.volunnear.services.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping(Routes.GET_FEEDBACKS_OF_ALL_ORGANIZATIONS)
    public Map<OrganizationResponseDTO, List<FeedbackResponseDTO>> getAllFeedbacksAboutAllOrganizations() {
        return feedbackService.getAllFeedbacksAboutAllOrganizations();
    }

    @Operation(summary = "Get feedbacks from selected organization", description = "Requires id of organization. Returns " +
            "Map<OrganizationResponseDTO, List<FeedbackResponseDTO>>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedbacks of current organization"),
            @ApiResponse(responseCode = "400", description = "There is no feedback about that organization"),
    })
    @GetMapping(Routes.GET_FEEDBACKS_FROM_CURRENT_ORGANIZATION)
    public ResponseEntity<?> getFeedbacksAboutCurrentOrganization(@RequestParam Integer id) {
        return feedbackService.getFeedbacksAboutCurrentOrganization(id);
    }

    @PostMapping(Routes.POST_FEEDBACK_ABOUT_ORGANIZATION)
    public ResponseEntity<String> postFeedbackAboutOrganization(@RequestBody FeedbackRequest feedbackRequest, Principal principal) {
        return feedbackService.postFeedbackAboutOrganization(feedbackRequest, principal);
    }

    @PutMapping(Routes.UPDATE_FEEDBACK_FOR_CURRENT_ORGANIZATION)
    public ResponseEntity<String> updateFeedbackInfoForCurrentOrganization(@RequestParam Integer idOfFeedback,
                                                                           @RequestBody FeedbackRequest feedbackRequest, Principal principal) {
        return feedbackService.updateFeedbackInfoForCurrentOrganization(idOfFeedback, feedbackRequest, principal);
    }

    @DeleteMapping(Routes.DELETE_FEEDBACK_ABOUT_ORGANIZATION)
    public ResponseEntity<String> deleteFeedbackAboutOrganization(@RequestParam Integer idOfFeedback, Principal principal) {
        return feedbackService.deleteFeedbackAboutOrganization(idOfFeedback, principal);
    }
}
