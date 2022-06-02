package com.tw.bootcamp.bookshop.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {
    @Bean
    public OpenAPI bookShopOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("basicScheme", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP).scheme("basic")))
                .info(new Info()
                        .title("Book Shop API")
                        .description("Book Shop swagger API documentation")
                        .version("v0.0.1")
                        .license(new License()
                                .name("ISC")
                                .url("http://springdoc.org"))
                );


    }

}