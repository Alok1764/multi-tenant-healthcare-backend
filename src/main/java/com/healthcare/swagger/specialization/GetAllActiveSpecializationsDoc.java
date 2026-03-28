package com.healthcare.swagger.specialization;

import com.healthcare.dto.response.SpecializationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
        summary = "Get all active specializations",
        description = "Returns a list of all currently active medical specializations. " +
                "Deactivated specializations are excluded from this response. " +
                "Publicly accessible — no authentication required."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active specializations retrieved successfully",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = SpecializationResponse.class))))
})
public @interface GetAllActiveSpecializationsDoc {}