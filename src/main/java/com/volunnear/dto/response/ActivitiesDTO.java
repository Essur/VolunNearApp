package com.volunnear.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ActivitiesDTO {
    private List<ActivityDTO> activities;
    private OrganizationResponseDTO organizationResponseDTO;

    public ActivitiesDTO() {
        activities = new ArrayList<>();
    }

    public void addActivity(ActivityDTO activity) {
        activities.add(activity);
    }
}
