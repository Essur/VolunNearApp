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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @ResponseStatus(value = HttpStatus.OK)
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
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(Routes.GET_FEEDBACKS_FROM_CURRENT_ORGANIZATION)
    public Map<OrganizationResponseDTO, List<FeedbackResponseDTO>> getFeedbacksAboutCurrentOrganization(@RequestParam Integer id) {
        return feedbackService.getFeedbacksAboutCurrentOrganization(id);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(Routes.POST_FEEDBACK_ABOUT_ORGANIZATION)
    public Integer postFeedbackAboutOrganization(@RequestBody FeedbackRequest feedbackRequest, Principal principal) {
        return feedbackService.postFeedbackAboutOrganization(feedbackRequest, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(Routes.UPDATE_FEEDBACK_FOR_CURRENT_ORGANIZATION)
    public FeedbackResponseDTO updateFeedbackInfoForCurrentOrganization(@RequestParam Integer idOfFeedback,
                                                                           @RequestBody FeedbackRequest feedbackRequest, Principal principal) {
        return feedbackService.updateFeedbackInfoForCurrentOrganization(idOfFeedback, feedbackRequest, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(Routes.DELETE_FEEDBACK_ABOUT_ORGANIZATION)
    public void deleteFeedbackAboutOrganization(@RequestParam Integer idOfFeedback, Principal principal) {
        feedbackService.deleteFeedbackAboutOrganization(idOfFeedback, principal);
    }
}
