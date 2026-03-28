
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
        summary = "Authenticate a user",
        description = "Validates credentials and returns a JWT access token and a refresh token."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = AuthenticationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request — missing or invalid fields",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized — incorrect email or password",
                content = @Content(mediaType = "application/json"))
})
public @interface LoginDoc {}