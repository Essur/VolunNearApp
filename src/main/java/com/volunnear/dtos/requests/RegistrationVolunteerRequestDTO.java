package com.volunnear.dtos.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationVolunteerRequestDTO {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}
