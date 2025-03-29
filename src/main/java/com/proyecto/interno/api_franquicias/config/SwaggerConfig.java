package com.proyecto.interno.api_franquicias.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiFranquiciasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Franquicias")
                        .description("API para el manejo de franquicias con WebFlux, MongoDB y programación reactiva")
                        .version("1.0.0"));
    }


}
