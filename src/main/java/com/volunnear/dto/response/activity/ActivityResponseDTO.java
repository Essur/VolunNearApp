package com.volunnear.dto.response.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponseDTO {
    private Long id;
    private Long organizationId;
    private String title;
    private String shortDescription;
    private String description;
    private String schedule;
    private LocalDateTime startedAt;
    private Point location;
}
