package com.volunnear.dto.geoInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeoCodingResponse(@JsonProperty("lat") String lat,
                                @JsonProperty("lon") String lon,
                                @JsonProperty("display_name") String displayName) {}
