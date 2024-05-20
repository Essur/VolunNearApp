package com.volunnear.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrganizationInfoRequestDTO {
    private String email;
    private String nameOfOrganization;
    private String country;
    private String city;
    private String address;
}
