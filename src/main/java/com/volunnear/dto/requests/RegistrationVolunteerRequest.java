package com.volunnear.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationVolunteerRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}
