package com.volunnear.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityChatLinkResponseDTO {
    private String socialNetwork;
    private String link;
    private String activityName;
    private Integer activityId;
}
