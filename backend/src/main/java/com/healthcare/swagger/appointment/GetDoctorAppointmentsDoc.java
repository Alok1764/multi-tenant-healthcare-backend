package com.healthcare.swagger.appointment;

import com.healthcare.dto.response.AppointmentResponse;
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
        summary = "Get all appointments for a doctor",
        description = "Returns a list of all appointments scheduled with the given doctor ID. " +
                "Restricted to authenticated users with the DOCTOR role only.",
        security = @SecurityRequirement(name = "bearerAuth"),
        parameters = @Parameter(
                name = "doctorId",
                description = "Unique identifier of the doctor",
                required = true,
                example = "3"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = AppointmentResponse.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only DOCTOR role is permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found — no doctor exists with the given ID",
                content = @Content(mediaType = "application/json"))
})
public @interface GetDoctorAppointmentsDoc {}