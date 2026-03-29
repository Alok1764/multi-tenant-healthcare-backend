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
        summary = "Get appointment by ID",
        description = "Retrieves the full details of a single appointment by its unique ID. " +
                "Accessible by any authenticated user.",
        security = @SecurityRequirement(name = "bearerAuth"),
        parameters = @Parameter(
                name = "id",
                description = "Unique identifier of the appointment",
                required = true,
                example = "42"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Appointment found and returned",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = AppointmentResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found — no appointment exists with the given ID",
                content = @Content(mediaType = "application/json"))
})
public @interface GetAppointmentDoc {}