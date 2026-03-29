package com.healthcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecializationCreateRequest {

    @NotBlank(message = "Specialization name is required")
    private String name;

    private String description;
}
