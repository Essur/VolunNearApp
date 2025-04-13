package com.volunnear.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequestInfoResponse {
    private Integer requestId;
    private Integer activityId;
    private String activityName;
}
