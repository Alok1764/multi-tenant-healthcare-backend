package com.healthcare.swagger.specialization;

import com.healthcare.dto.response.SpecializationResponse;
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
        summary = "Get specialization by ID",
        description = "Retrieves the details of a specific specialization by its unique ID. " +
                "Publicly accessible — no authentication required.",
        parameters = @Parameter(
                name = "id",
                description = "Unique identifier of the specialization",
                required = true,
                example = "1"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Specialization retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = SpecializationResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not found — no specialization exists with the given ID",
                content = @Content(mediaType = "application/json"))
})
public @interface GetSpecializationByIdDoc {}