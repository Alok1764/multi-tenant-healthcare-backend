
package com.healthcare.swagger.auth;

import com.healthcare.dto.response.RefreshTokenResponse;
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
        summary = "Refresh the JWT access token",
        description = "Accepts a valid refresh token and issues a new JWT access token."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = RefreshTokenResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request — token missing or malformed",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized — token expired or already invalidated",
                content = @Content(mediaType = "application/json"))
})
public @interface RefreshTokenDoc {}