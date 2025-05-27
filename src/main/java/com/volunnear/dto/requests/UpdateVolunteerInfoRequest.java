package com.volunnear.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVolunteerInfoRequest {
    private String email;
    private String firstName;
    private String lastName;
}
