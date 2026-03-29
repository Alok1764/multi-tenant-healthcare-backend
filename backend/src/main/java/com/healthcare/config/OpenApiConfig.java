package com.healthcare.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Healthcare Management API")
                        .version("1.0.0")
                        .description("REST API for managing hospitals, doctors, patients, appointments, and payments"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .schemaRequirement("bearerAuth", new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .tags(List.of(
                        new Tag().name("Authentication")
                                .description("User registration, login, token refresh, and logout"),
                        new Tag().name("Patient Management")
                                .description("Endpoints for adding and managing patient profiles"),
                        new Tag().name("Doctor Management")
                                .description("Endpoints for onboarding doctors, browsing profiles, and managing availability"),
                        new Tag().name("Appointment Management")
                                .description("Endpoints for booking, viewing, and cancelling appointments"),
                        new Tag().name("Hospital Management")
                                .description("Endpoints for registering and browsing hospitals"),
                        new Tag().name("Appointment Slots")
                                .description("Endpoints for managing doctor availability slots"),
                        new Tag().name("Specialization Management")
                                .description("Endpoints for managing medical specializations"),
                        new Tag().name("Medical Records")
                                .description("Endpoints for creating and retrieving patient medical records"),
                        new Tag().name("Payment Management")
                                .description("Endpoints for processing and retrieving appointment payments")
                ));
    }
}