package com.volunnear.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private Integer id;
    private String city;
    private String country;
    private Instant dateOfPlace;
    private String description;
    private String title;
    private String kindOfActivity;
}
