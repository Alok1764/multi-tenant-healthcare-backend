package com.healthcare.dto.request;

import com.healthcare.model.enums.BloodGroup;
import com.healthcare.model.enums.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAddRequest {

    @NotNull(message = "Hospital ID is required")
    private Long hospitalId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private Gender gender;

    private BloodGroup bloodGroup;

    @Size(max = 1000)
    private String address;

    private String emergencyContactName;

    @Size(max = 20)
    private String emergencyContactPhone;

    private String medicalHistory;

    private String allergies;

    @Size(max = 500)
    private String profileImageUrl;
}
