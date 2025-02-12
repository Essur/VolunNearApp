package com.volunnear.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerInfo {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
}
