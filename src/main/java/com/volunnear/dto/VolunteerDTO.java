package com.volunnear.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerDTO {
    private String nameOfUser;
    private Credentials credentials;
}
