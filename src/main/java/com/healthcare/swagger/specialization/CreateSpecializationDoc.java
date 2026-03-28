package com.healthcare.swagger.specialization;

import com.healthcare.dto.response.SpecializationResponse;
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
        summary = "Create a specialization",
        description = "Registers a new medical specialization in the system (e.g. Cardiology, Orthopedics). " +
                "Only a hospital admin can perform this action. " +
                "Specializations are used to categorize doctors and filter appointment slots.",
        security = @SecurityRequirement(name = "bearerAuth")
)
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "Specialization created successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = SpecializationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request — invalid or missing fields in the request body",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only ROLE_HOSPITAL_ADMIN is permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Conflict — a specialization with the same name already exists",
                content = @Content(mediaType = "application/json"))
})
public @interface CreateSpecializationDoc {}