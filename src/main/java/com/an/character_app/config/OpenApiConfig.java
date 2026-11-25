package com.an.character_app.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info()
                .title("D&D 2024 Character Manager API")
                .description("Create characters and get computed sheets using the 2024 rules.")
                .version("0.1.0"));
    }
}
