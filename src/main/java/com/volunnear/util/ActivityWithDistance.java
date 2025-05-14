package com.volunnear.util;

import com.volunnear.entity.activities.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityWithDistance {
    private Activity activity;
    private double distance;
}
