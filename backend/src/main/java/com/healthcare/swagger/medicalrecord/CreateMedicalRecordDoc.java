package com.healthcare.swagger.medicalrecord;

import com.healthcare.dto.response.MedicalRecordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Create a medical record",
        description = "Allows an authenticated doctor to create a new medical record tied to a completed appointment. " +
                "The record typically includes diagnosis, prescriptions, and clinical notes.",
        security = @SecurityRequirement(name = "bearerAuth")
)
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "Medical record created successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = MedicalRecordResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request — invalid or missing fields in the request body",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only ROLE_DOCTOR is permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found — referenced appointment or patient does not exist",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Conflict — a medical record already exists for this appointment",
                content = @Content(mediaType = "application/json"))
})
public @interface CreateMedicalRecordDoc {}