package com.volunnear.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
    private Credentials credentials;
    private String nameOfOrganisation;
    private String country;
    private String city;
    private String address;
}
