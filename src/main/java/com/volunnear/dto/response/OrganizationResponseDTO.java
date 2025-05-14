package com.volunnear.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationResponseDTO {
    private Integer id;
    private String organizationName;
    private String country;
    private String city;
    private String address;
    private String email;
}
