package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerOpenApi {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users API")
                        .version("1.0")
                        .description("Users API")
                )
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                ).addSecurityItem(
                        new SecurityRequirement()
                                .addList("bearer-key", Arrays.asList("read", "write"))
                );
    }

    @Bean
    public GroupedOpenApi authorizationApi() {
        return GroupedOpenApi.builder()
            .group("Authorization APIs")
            .pathsToMatch("/api/auth/**")
            .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
            .group("Admin APIs")
            .pathsToMatch("/admin/**")
            .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
            .group("User APIs")
            .pathsToMatch("/user/**")
            .build();
    }

}
