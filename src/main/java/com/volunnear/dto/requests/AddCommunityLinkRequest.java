package com.volunnear.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCommunityLinkRequest {
    private String link;
    private String socialNetwork;
}
