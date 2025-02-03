package com.volunnear.services.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerPreferenceDTO {
    private Integer preferenceId;
    private Integer volunteerId;
    private String preferenceName;
}
