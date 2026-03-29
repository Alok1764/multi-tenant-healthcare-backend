package com.healthcare.swagger.doctor;

import com.healthcare.dto.response.DoctorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
        summary = "Get all doctors",
        description = "Returns a paginated list of all doctors registered in the system. " +
                "Publicly accessible — no authentication required. " +
                "Use pageNo and pageSize to control pagination."
)
@Parameters({
        @Parameter(name = "pageNo",   description = "Zero-based page index (default: 0)",  example = "0"),
        @Parameter(name = "pageSize", description = "Number of records per page (default: 10)", example = "10")
})
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctors retrieved successfully",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = DoctorResponse.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request — invalid pagination parameters",
                content = @Content(mediaType = "application/json"))
})
public @interface GetAllDoctorsDoc {}