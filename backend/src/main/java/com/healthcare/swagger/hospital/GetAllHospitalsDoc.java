package com.healthcare.swagger.hospital;

import com.healthcare.dto.response.HospitalResponse;
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
        summary = "Get all hospitals",
        description = "Returns a list of all hospitals registered in the system. " +
                "Publicly accessible — no authentication required."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Hospitals retrieved successfully",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = HospitalResponse.class))))
})
public @interface GetAllHospitalsDoc {}