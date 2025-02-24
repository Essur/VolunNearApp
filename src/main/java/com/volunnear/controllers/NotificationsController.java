package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.response.OrganizationResponseDTO;
import com.volunnear.services.notifications.EmailNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationsController {
    private final EmailNotificationService emailNotificationService;

    @Operation(summary = "Get all subscriptions of volunteer", description = "Returns List<OrganizationResponseDTO>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List<OrganizationResponseDTO> (list of subscriptions with info about organizations)",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrganizationResponseDTO.class)))),
            @ApiResponse(responseCode = "400", description = "List of subscriptions is empty!")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(Routes.GET_ALL_SUBSCRIPTIONS_OF_VOLUNTEER)
    public List<OrganizationResponseDTO> getAllSubscriptionsOfVolunteer(Principal principal) {
        return emailNotificationService.getAllSubscriptionsOfVolunteer(principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.GET_NOTIFICATION_SUBSCRIPTION_STATUS)
    public void getNotificationSubscriptionStatus(@RequestParam Integer organizationId, Principal principal) {
        emailNotificationService.getNotificationSubscriptionStatus(organizationId, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(Routes.SUBSCRIBE_TO_NOTIFICATIONS_BY_ID_OF_ORGANIZATION)
    public void subscribeToNotificationsByIdOfOrganization(@RequestParam Integer organizationId, Principal principal) {
        emailNotificationService.subscribeToNotificationByIdOfOrganization(organizationId, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(Routes.UNSUBSCRIBE_FROM_NOTIFICATIONS_BY_ID_OF_ORGANIZATION)
    public void unsubscribeFromNotificationsByIdOfOrganizations(@RequestParam Integer organizationId, Principal principal) {
        emailNotificationService.unsubscribeFromNotificationOfOrganization(organizationId, principal);
    }
}
