package com.volunnear.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddActivityLinkRequest {
    private Integer activityId;
    private String link;
    private String socialNetwork;
}
