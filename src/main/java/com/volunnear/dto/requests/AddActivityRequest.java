package com.volunnear.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddActivityRequest {
    private String title;
    private String description;
    private String country;
    private String city;
    private String kindOfActivity;
    private double latitude;
    private double longitude;
}
