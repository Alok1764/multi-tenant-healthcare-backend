package com.healthcare.swagger.appointment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
        summary = "Cancel an appointment",
        description = "Cancels an existing appointment by its ID. " +
                "Both the patient who booked it and the assigned doctor are permitted to cancel. " +
                "No content is returned on success.",
        security = @SecurityRequirement(name = "bearerAuth"),
        parameters = @Parameter(
                name = "id",
                description = "Unique identifier of the appointment to cancel",
                required = true,
                example = "42"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "204", description = "Appointment cancelled successfully — no content returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only PATIENT or DOCTOR roles are permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found — no appointment exists with the given ID",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Conflict — appointment is already cancelled or completed",
                content = @Content(mediaType = "application/json"))
})
public @interface CancelAppointmentDoc {}