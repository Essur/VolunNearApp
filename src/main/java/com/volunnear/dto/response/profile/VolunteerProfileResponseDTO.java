package com.volunnear.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerProfileResponseDTO {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String middleName;
    private String about;
    private LocalDate birthday;
    private LocalDate created;
    private String phone;
}
