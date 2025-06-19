package com.volunnear.dto.response.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCardDTO {
    private Long id;
    private Long organizationId;
    private String title;
    private String shortDescription;
    private String schedule;
    private LocalDateTime startedAt;
    private String country;
    private String city;
    private String address;
}
