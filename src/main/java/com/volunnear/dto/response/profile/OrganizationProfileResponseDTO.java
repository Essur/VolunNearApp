package com.volunnear.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationProfileResponseDTO {
    private String email;
    private String username;
    private String organizationName;
    private String description;
    private String country;
    private String city;
    private String address;
    private String phone;
    private String website;
    private LocalDate created;
}
