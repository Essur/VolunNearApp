package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.requests.AddActivityLinkRequest;
import com.volunnear.dto.requests.AddCommunityLinkRequest;
import com.volunnear.service.SocialMediaIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class SocialMediaIntegrationController {
    private final SocialMediaIntegrationService socialMediaIntegrationService;

    @Operation(summary = "Add chat link to activity", description = "Requires organization account (token), link and idOfActivity")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(Routes.ADD_CHAT_LINK_FOR_ACTIVITY)
    public Integer addChatLinkToActivity(@RequestBody AddActivityLinkRequest addActivityLinkRequest,
                                                   Principal principal) {
        return socialMediaIntegrationService.addChatLinkToActivity(addActivityLinkRequest, principal);
    }

    @Operation(summary = "Add community link", description = "Requires organization account (token) and link")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(Routes.ADD_COMMUNITY_LINK)
    public Integer addCommunityLink(@RequestBody AddCommunityLinkRequest linkRequestDTO,
                                              Principal principal) {
        return socialMediaIntegrationService.addCommunityLink(linkRequestDTO, principal);
    }

    @Operation(summary = "Get chat link by id of activity", description = "Method requires idOfActivity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link founded"),
            @ApiResponse(responseCode = "400", description = "Bad id of activity!")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(Routes.GET_CHAT_LINK_BY_ACTIVITY)
    public String getChatLinkByActivity(@RequestParam Integer idOfActivity) {
        return socialMediaIntegrationService.getChatLinkByActivityId(idOfActivity);
    }

    @Operation(summary = "Get community link by id of organization", description = "Method requires idOfOrganization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link founded"),
            @ApiResponse(responseCode = "400", description = "Bad id of organization!")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(Routes.GET_COMMUNITY_LINK_BY_ORGANIZATION)
    public String getCommunityLinkByOrganization(@RequestParam Integer idOfOrganization) {
        return socialMediaIntegrationService.getCommunityLinkByOrganizationId(idOfOrganization);
    }
}
