package com.volunnear.dto.geoInfo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String address;

    public String toQuery() {
        return String.join(", ", address, city, country);
    }
}
