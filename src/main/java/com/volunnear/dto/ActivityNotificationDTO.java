package com.volunnear.dto;

import com.volunnear.dto.response.ActivityDTO;
import com.volunnear.dto.response.OrganizationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityNotificationDTO {
    private ActivityDTO activityDTO;
    private OrganizationResponseDTO organizationResponseDTO;
}
