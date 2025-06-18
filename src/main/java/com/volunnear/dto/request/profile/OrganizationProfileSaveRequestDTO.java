package com.volunnear.dto.request.profile;

import com.volunnear.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationProfileSaveRequestDTO {
    @NotBlank
    private String organizationName;
    @NotBlank
    private String description;
    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String address;
    @NotBlank
    @ValidPhoneNumber
    private String phone;
    @NotBlank
    private String website;
}
