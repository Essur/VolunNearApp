package com.volunnear.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerPreferenceDTO {
    private Integer id;
    private String username;
    private double latitude;
    private double longitude;
}