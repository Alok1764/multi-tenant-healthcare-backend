package com.healthcare.swagger.appointmentslot;

import com.healthcare.dto.response.AppointmentSlotResponse;
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
        summary = "Create an appointment slot",
        description = "Allows a doctor or hospital admin to create a new available appointment slot. " +
                "The slot defines the doctor, date, time, and duration during which patients can book appointments.",
        security = @SecurityRequirement(name = "bearerAuth")
)
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "Slot created successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = AppointmentSlotResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request — invalid or missing fields in the request body",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only ROLE_DOCTOR or ROLE_HOSPITAL_ADMIN are permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Conflict — an overlapping slot already exists for this doctor",
                content = @Content(mediaType = "application/json"))
})
public @interface CreateSlotDoc {}