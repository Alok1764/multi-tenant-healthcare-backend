package com.healthcare.swagger.specialization;

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
        summary = "Deactivate a specialization",
        description = "Soft-deletes a specialization by marking it as inactive. " +
                "The record is retained in the database but will no longer appear in active listings " +
                "or be assignable to doctors. Only a hospital admin can perform this action.",
        security = @SecurityRequirement(name = "bearerAuth"),
        parameters = @Parameter(
                name = "id",
                description = "Unique identifier of the specialization to deactivate",
                required = true,
                example = "1"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "204", description = "Specialization deactivated successfully — no content returned"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only ROLE_HOSPITAL_ADMIN is permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found — no specialization exists with the given ID",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Conflict — specialization is already deactivated",
                content = @Content(mediaType = "application/json"))
})
public @interface DeactivateSpecializationDoc {}