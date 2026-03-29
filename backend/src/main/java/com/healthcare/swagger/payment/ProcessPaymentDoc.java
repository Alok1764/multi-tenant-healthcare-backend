package com.healthcare.swagger.payment;

import com.healthcare.dto.response.PaymentResponse;
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
        summary = "Process appointment payment",
        description = "Initiates and processes a payment for an existing appointment. " +
                "Only the patient associated with the appointment can trigger this action. " +
                "Ensure the appointment is in a payable state before calling this endpoint — " +
                "already-paid or cancelled appointments will be rejected.",
        security = @SecurityRequirement(name = "bearerAuth")
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment processed successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = PaymentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request — invalid or missing fields in the request body",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Unauthorized — valid Bearer token required",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Forbidden — only ROLE_PATIENT is permitted",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not found — referenced appointment does not exist",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Conflict — payment already exists for this appointment",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "422", description = "Unprocessable entity — payment gateway rejected the transaction",
                content = @Content(mediaType = "application/json"))
})
public @interface ProcessPaymentDoc {}