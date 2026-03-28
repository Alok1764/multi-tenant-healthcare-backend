package com.healthcare.swagger.payment;

import com.healthcare.dto.response.PaymentResponse;
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
        summary = "Get payment details by appointment",
        description = "Retrieves the payment record associated with a specific appointment ID. " +
                "Returns the payment status, amount, method, and timestamp. " +
                "Accessible by the patient who made the payment or an admin.",
        security = @SecurityRequirement(name = "bearerAuth"),
        parameters = @Parameter(
                name = "appointmentId",
                description = "Unique identifier of the appointment whose payment details are being requested",
                required = true,
                example = "42"
        )
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = PaymentResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only ROLE_PATIENT or ROLE_ADMIN are permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found — no payment record exists for the given appointment ID",
                content = @Content(mediaType = "application/json"))
})
public @interface GetPaymentStatusDoc {}