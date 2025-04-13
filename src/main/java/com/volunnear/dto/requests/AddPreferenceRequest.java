package com.volunnear.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPreferenceRequest {
    private double latitude;
    private double longitude;
}
