
package com.healthcare.swagger.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Log out the current user",
        description = "Invalidates the refresh token server-side, ending the user's session."
)
@ApiResponses({
        @ApiResponse(responseCode = "204", description = "Logout successful — no content returned"),
        @ApiResponse(responseCode = "400", description = "Bad request — token missing or malformed",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized — token not found or already expired",
                content = @Content(mediaType = "application/json"))
})
public @interface LogoutDoc {}