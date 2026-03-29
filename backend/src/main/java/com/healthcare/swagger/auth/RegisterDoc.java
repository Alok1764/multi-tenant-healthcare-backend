
package com.healthcare.swagger.auth;

import com.healthcare.dto.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
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
        summary = "Register a new user",
        description = "Creates a new user account and returns a JWT access token along with a refresh token."
)
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "User registered successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = AuthenticationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed — missing or malformed fields",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Conflict — email already exists",
                content = @Content(mediaType = "application/json"))
})
public @interface RegisterDoc {}