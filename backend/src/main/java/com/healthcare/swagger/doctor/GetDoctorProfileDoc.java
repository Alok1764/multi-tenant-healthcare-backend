package com.healthcare.swagger.doctor;

import com.healthcare.dto.response.DoctorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Get doctor profile by ID",
        description = "Retrieves the full profile of a specific doctor by their unique ID. " +
                "Publicly accessible — no authentication required.",
        parameters = @Parameter(
                name = "id",
                description = "Unique identifier of the doctor",
                required = true,
                example = "3"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor profile retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = DoctorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not found — no doctor exists with the given ID",
                content = @Content(mediaType = "application/json"))
})
public @interface GetDoctorProfileDoc {}