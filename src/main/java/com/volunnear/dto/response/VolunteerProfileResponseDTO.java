package com.volunnear.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerProfileResponseDTO {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private List<ActivitiesDTO> activitiesDTO;
}
