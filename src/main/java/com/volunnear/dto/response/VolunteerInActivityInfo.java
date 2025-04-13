package com.volunnear.dto.response;

import com.volunnear.dto.VolunteerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VolunteerInActivityInfo extends VolunteerInfo {
    private Instant acceptedDate;

    public VolunteerInActivityInfo(String email, String username, String firstName, String lastName, Instant acceptedDate) {
        super(email, username, firstName, lastName);
        this.acceptedDate = acceptedDate;
    }
}
