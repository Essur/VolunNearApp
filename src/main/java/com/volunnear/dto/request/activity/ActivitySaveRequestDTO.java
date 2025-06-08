package com.volunnear.dto.request.activity;

import com.volunnear.dto.geoInfo.AddressDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySaveRequestDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String schedule;
    @Size(min = 1, max = 255)
    @NotBlank
    private String shortDescription;
    @NotBlank
    private String description;
    private AddressDTO location;
}
