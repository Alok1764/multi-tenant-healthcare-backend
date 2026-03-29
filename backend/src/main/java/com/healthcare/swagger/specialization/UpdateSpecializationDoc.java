package com.healthcare.swagger.specialization;

import com.healthcare.dto.response.SpecializationResponse;
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
        summary = "Update a specialization",
        description = "Updates the details of an existing specialization by its unique ID. " +
                "Only a hospital admin can perform this action. " +
                "Only fields provided in the request body will be updated.",
        security = @SecurityRequirement(name = "bearerAuth"),
        parameters = @Parameter(
                name = "id",
                description = "Unique identifier of the specialization to update",
                required = true,
                example = "1"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Specialization updated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = SpecializationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request — invalid or missing fields in the request body",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only ROLE_HOSPITAL_ADMIN is permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found — no specialization exists with the given ID",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Conflict — another specialization with the same name already exists",
                content = @Content(mediaType = "application/json"))
})
public @interface UpdateSpecializationDoc {}