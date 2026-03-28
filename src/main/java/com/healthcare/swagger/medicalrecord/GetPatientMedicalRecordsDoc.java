package com.healthcare.swagger.medicalrecord;

import com.healthcare.dto.response.MedicalRecordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
        summary = "Get all medical records for a patient",
        description = "Returns the full medical history for a specific patient identified by their ID. " +
                "Accessible by the patient themselves, their treating doctor, or an admin. " +
                "Records are returned in reverse chronological order.",
        security = @SecurityRequirement(name = "bearerAuth"),
        parameters = @Parameter(
                name = "patientId",
                description = "Unique identifier of the patient",
                required = true,
                example = "7"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Medical records retrieved successfully",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = MedicalRecordResponse.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only ROLE_DOCTOR, ROLE_PATIENT, or ROLE_ADMIN are permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found — no patient exists with the given ID",
                content = @Content(mediaType = "application/json"))
})
public @interface GetPatientMedicalRecordsDoc {}