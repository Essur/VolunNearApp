package com.volunnear.dto.response;

import com.volunnear.dto.VolunteerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizationActivityRequestInfoResponse extends ActivityRequestInfoResponse {
    private VolunteerInfo volunteerInfo;

    public OrganizationActivityRequestInfoResponse(Integer requestId, Integer activityId, String activityName, VolunteerInfo volunteerInfo) {
        super(requestId, activityId, activityName);
        this.volunteerInfo = volunteerInfo;
    }
}
