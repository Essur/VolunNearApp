package com.volunnear.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDTO {
    private Integer id;
    private int rate;
    private String description;
    private String realNameOfUser;
    private String username;
}
