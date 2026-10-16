package com.rajesh.jobportal.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jobPortalOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Job Portal Management System API")
                                .version("1.0")
                                .description(
                                        "REST API for managing users, jobs and job applications"
                                )
                );
    }
}