package com.healthcare.dto.request;

import lombok.Data;

@Data
public class SpecializationUpdateRequest {

    private String description;
    private Boolean isActive;
}
