package com.volunnear.dto.request.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVolunteerProfileInfoRequestDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String middleName;
    private String about;
    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @NotBlank
    private String phone;
}
