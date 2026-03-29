package com.healthcare.swagger.hospital;

import com.healthcare.dto.response.HospitalResponse;
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
        summary = "Get hospital by ID",
        description = "Retrieves the full details of a specific hospital by its unique ID. " +
                "Publicly accessible — no authentication required.",
        parameters = @Parameter(
                name = "id",
                description = "Unique identifier of the hospital",
                required = true,
                example = "1"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Hospital retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = HospitalResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not found — no hospital exists with the given ID",
                content = @Content(mediaType = "application/json"))
})
public @interface GetHospitalDoc {}