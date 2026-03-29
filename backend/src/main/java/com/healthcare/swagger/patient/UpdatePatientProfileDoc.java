package com.healthcare.swagger.patient;

import com.healthcare.dto.response.PatientProfileResponse;
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
        summary = "Update my patient profile",
        description = "Updates the profile of the currently authenticated patient. " +
                "The patient is identified from the Bearer token — no ID parameter is required. " +
                "Only fields provided in the request body will be updated; " +
                "omitted fields retain their current values.",
        security = @SecurityRequirement(name = "bearerAuth")
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient profile updated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = PatientProfileResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request — invalid or missing fields in the request body",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only ROLE_PATIENT is permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found — authenticated user does not have a patient profile",
                content = @Content(mediaType = "application/json"))
})
public @interface UpdatePatientProfileDoc {}