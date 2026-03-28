package com.healthcare.swagger.appointment;

import com.healthcare.dto.response.AppointmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
        summary = "Book a new appointment",
        description = "Allows an authenticated patient to book an appointment with a doctor. " +
                "Requires an idempotency key in the request header to prevent duplicate bookings " +
                "on retried requests.",
        security = @SecurityRequirement(name = "bearerAuth"),
        parameters = @Parameter(
                name = "X-Idempotency-Key",
                description = "Unique key (e.g. UUID) to ensure duplicate requests do not create duplicate appointments",
                required = true,
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "Appointment booked successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = AppointmentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request — invalid or missing fields",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only ROLE_PATIENT can book appointments",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Conflict — duplicate request detected via idempotency key",
                content = @Content(mediaType = "application/json"))
})
public @interface BookAppointmentDoc {}