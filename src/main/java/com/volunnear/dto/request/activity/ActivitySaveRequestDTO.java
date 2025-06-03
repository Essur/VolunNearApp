package com.volunnear.dto.request.activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private String description;
    @NotNull
    private Double lat;
    @NotNull
    private Double lng;
}
